package com.genedu.subscription.service.impl;

import com.genedu.commonlibrary.enumeration.TransactionStatus;
import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.subscription.configuration.StripeConfig;
import com.genedu.subscription.dto.WebhookRequest;
import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.dto.userbillingaccount.UserBillingAccountResponseDTO;
import com.genedu.subscription.dto.usertransaction.UserTransactionRequestDTO;
import com.genedu.subscription.model.SubscriptionPlan;
import com.genedu.subscription.model.UserBillingAccount;
import com.genedu.subscription.service.PaymentGatewayService;
import com.genedu.subscription.service.UserTransactionService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
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
    private final UserBillingAccountServiceImpl userBillingAccountServiceImpl;

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
    public void cancelSubscription(String gatewaySubscriptionId) {

    }

    @Override
    public boolean isWebhookSignatureValid(WebhookRequest webhookRequest) {
        return false;
    }

    public void handleWebhookEvent(WebhookRequest webhookRequest) {
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
        log.info("✅ Checkout session completed. Customer: {}, Subscription: {}", customerId, subscriptionId);

        // TODO: Update DB: mark account active, save Stripe IDs
    }

    private void handleInvoicePaid(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Invoice data missing"));

        String subscriptionId = getSubscriptionIdFromInvoice(invoice);
        String customerId = invoice.getCustomer();
        BigDecimal amount = BigDecimal.valueOf(invoice.getAmountPaid());

        userTransactionService.createTransaction(
                new UserTransactionRequestDTO(
                        customerId,
                        amount,
                        TransactionStatus.SUCCESS.name()
                )
        );

//        SubscriptionServiceImpl


        log.info("✅ Invoice paid. Transaction recorded for subscription: {}", subscriptionId);

        // TODO: Mark billing as paid, ensure access is active
    }

    private void handlePaymentFailed(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Invoice data missing"));

        String subscriptionId = getSubscriptionIdFromInvoice(invoice);
        log.warn("⚠️ Invoice payment failed for subscription: {}", subscriptionId);

        // TODO: Notify user, mark account for retry or downgrade
    }

    private void handleSubscriptionCancelled(Event event) {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Subscription data missing"));

        String stripeCustomerId = subscription.getCustomer();
        log.info("❌ Subscription canceled for customer: {}", stripeCustomerId);

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

    /**
     * Helper để lấy Subscription ID từ Invoice trong trường hợp Stripe SDK không expose trực tiếp.
     */
    private String getSubscriptionIdFromInvoice(Invoice invoice) {
//        try {
//            return invoice.getSubscription(); // nếu IDE nhận được thì dùng
//        } catch (Exception e) {
//            // fallback nếu SDK không hỗ trợ getter
//            try {
//                return invoice.getRawJsonObject().get("subscription").getAsString();
//            } catch (Exception ex) {
//                log.error("❌ Failed to extract subscription ID from invoice", ex);
//                return null;
//            }
//        }
        return "";
    }
}
