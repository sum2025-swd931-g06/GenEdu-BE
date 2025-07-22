package com.genedu.subscription.service.impl;

import com.genedu.commonlibrary.enumeration.PaymentStatus;
import com.genedu.commonlibrary.enumeration.TransactionStatus;
import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.subscription.configuration.StripeConfig;
import com.genedu.subscription.dto.WebhookRequest;
import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.dto.userbillingaccount.UserBillingAccountResponseDTO;
import com.genedu.subscription.dto.usertransaction.UserTransactionRequestDTO;
import com.genedu.subscription.repository.SubscriptionRepository;
import com.genedu.subscription.service.PaymentGatewayService;
import com.genedu.subscription.service.SubscriptionService;
import com.genedu.subscription.service.UserBillingAccountService;
import com.genedu.subscription.service.UserTransactionService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeService implements PaymentGatewayService {
    private final StripeConfig stripeConfig;
    private final UserTransactionService userTransactionService;
    private final UserBillingAccountService userBillingAccountService;
    private final SubscriptionService subscriptionService;

    @Override
    public Map<String, Object> createCheckoutSession(UserBillingAccountResponseDTO billing, SubscriptionPlanResponseDTO subscriptionPlan) throws StripeException {

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setCustomer(billing.paymentGatewayCustomerId())
                .setSuccessUrl(stripeConfig.getSuccessUrl())
                .setCancelUrl(stripeConfig.getCancelUrl())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(subscriptionPlan.stripePriceId())
                                .setQuantity(1L)
                                .build()
                )
                .build();
        Session session = Session.create(params);
        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", session.getId());
        result.put("sessionUrl", session.getUrl());
        return result;
    }

    @Override
    public boolean isWebhookSignatureValid(WebhookRequest webhookRequest) {
        return false;
    }

    public void handleWebhookEvent(WebhookRequest webhookRequest) throws StripeException {
        String payload = webhookRequest.payload();
        String sigHeader = webhookRequest.signature();

        Event event;
        try {
            event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    stripeConfig.getWebhookSecret()
            );
        } catch (SignatureVerificationException e) {
            log.error("⚠️  Webhook signature verification failed.", e);
            throw new BadRequestException("Webhook signature verification failed");
        }

        switch (event.getType()) {
            case "checkout.session.completed" -> handleCheckoutSessionCompleted(event);
            case "invoice.paid" -> handleInvoicePaid(event);
            case "invoice.payment_failed" -> handlePaymentFailed(event);
            case "customer.subscription.deleted" -> handleSubscriptionCancelled(event);
            case "customer.created" -> handleCustomerCreated(event);
            case "customer.updated" -> handleCustomerUpdated(event);
            case "customer.subscription.created" -> handleSubscriptionCreated(event);
            default -> log.info("Unhandled event type: {}", event.getType());
        }
    }

    private void handleCheckoutSessionCompleted(Event event) {
        Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Session data missing"));

        String customerId = session.getCustomer();
        String subscriptionId = session.getSubscription();

        try {
            // Retrieve the full session to get the latest payment status
            session = Session.retrieve(session.getId());

            // Check payment status
            if (!"paid".equals(session.getPaymentStatus())) {
                log.warn("⚠️ Payment not confirmed yet for session: {}, status: {}",
                        session.getId(), session.getPaymentStatus());
                return; // Exit without creating subscription until payment is confirmed
            }

            // Payment confirmed, retrieve subscription details
            Subscription subscription = Subscription.retrieve(subscriptionId);

            String priceId = subscription.getItems().getData().get(0).getPrice().getId(); // hoặc getPlan().getId()
            String planId = Price.retrieve(priceId).getProduct();

            Customer customer = Customer.retrieve(customerId);
            String email = customer.getEmail();
            String userName = customer.getName();
            log.info("Payment successful. Creating subscription with plan: {}", planId);

            // Create subscription in our system
            subscriptionService.startSubscription(
                    new SubscriptionRequestDTO(
                            customerId,
                            planId,
                            subscriptionId,
                            true,
                            subscription.getStatus(),
                            email,
                            userName
                    )
            );

            log.info("Subscription created. Customer: {}, Subscription: {}", customerId, subscriptionId);
        } catch (StripeException e) {
            log.error("Failed to process subscription after payment for ID: {}", subscriptionId, e);
            throw new RuntimeException("Error processing paid subscription", e);
        }
    }

    private void handleInvoicePaid(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Invoice data missing"));

        String customerId = invoice.getCustomer();
        BigDecimal amount = BigDecimal.valueOf(invoice.getAmountPaid());

        userTransactionService.createTransaction(
                new UserTransactionRequestDTO(
                        customerId,
                        amount,
                        TransactionStatus.SUCCESS.name()
                )
        );
        updateSubscriptionStatus(customerId, true);

        log.info("✅ Invoice paid. Transaction recorded for customer: {}, amount: {}", customerId, amount);
        // TODO: Mark billing as paid, ensure access is active
    }

    private void handlePaymentFailed(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Invoice data missing"));

        String customerId = invoice.getCustomer();
        BigDecimal amount = BigDecimal.valueOf(invoice.getAmountPaid());

        userTransactionService.createTransaction(
                new UserTransactionRequestDTO(
                        customerId,
                        amount,
                        TransactionStatus.FAILED.name()
                )
        );

        updateSubscriptionStatus(customerId, false);

        log.warn("⚠️ Invoice payment failed for customer: {}, amount: {}", customerId, amount);

        // TODO: Notify user, mark account for retry or downgrade
    }

    private void handleSubscriptionCancelled(Event event) throws StripeException {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Subscription data missing"));

        String stripeCustomerId = subscription.getCustomer();

        updateSubscriptionStatus(stripeCustomerId, false);

        String subscriptionStatus = subscription.getStatus();
        subscriptionService.turnOffAutoRenew(subscription.getId(), subscriptionStatus);

        log.info("❌ Subscription canceled for customer: {}", stripeCustomerId);
        // Update local subscription status
        // TODO: Revoke access, update subscription status in DB
    }

    private void handleCustomerCreated(Event event) {
        Customer customer = (Customer) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Customer data missing"));

        log.info("👤 New customer created: {}", customer.getId());

        // TODO: Sync metadata or email to local profile if needed
    }

    private void handleCustomerUpdated(Event event) {
        Customer customer = (Customer) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Customer data missing"));

        log.info("✏️ Customer updated: {}", customer.getId());

        // TODO: Update local user info if email or metadata changed
    }

    private void handleSubscriptionCreated(Event event) {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Subscription data missing"));

        log.info("📦 Subscription created: {} for customer: {}", subscription.getId(), subscription.getCustomer());

        // TODO: Save new subscription in DB, activate user benefits
    }

    private void updateSubscriptionStatus(String stripeCustomerId, boolean isSubscriptionActive) {
        try {
            // Update the subscription status in your database
            userBillingAccountService.updateSubscriptionStatus(
                    stripeCustomerId,
                    isSubscriptionActive
            );
            log.info("Subscription status updated for customer: {}", stripeCustomerId);
        } catch (Exception e) {
            log.error("Failed to update subscription status for customer: {}", stripeCustomerId, e);
            throw new RuntimeException("Error updating subscription status", e);
        }
    }

}
