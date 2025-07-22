package com.genedu.subscription.service.impl;

import com.genedu.commonlibrary.enumeration.PaymentStatus;
import com.genedu.commonlibrary.enumeration.SubscriptionStatus;
import com.genedu.commonlibrary.enumeration.TransactionStatus;
import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.subscription.configuration.StripeConfig;
import com.genedu.subscription.dto.WebhookRequest;
import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.dto.userbillingaccount.UserBillingAccountResponseDTO;
import com.genedu.subscription.dto.usertransaction.UserTransactionRequestDTO;
import com.genedu.subscription.service.PaymentGatewayService;
import com.genedu.subscription.service.SubscriptionService;
import com.genedu.subscription.service.UserBillingAccountService;
import com.genedu.subscription.service.UserTransactionService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

            log.info("Payment successful. Creating subscription with plan: {}", planId);

            // Create subscription in our system
            subscriptionService.startSubscription(
                    new SubscriptionRequestDTO(
                            customerId,
                            planId,
                            true,
                            PaymentStatus.COMPLETED.getValue()
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
        userBillingAccountService.updateSubscriptionStatus(customerId, Boolean.TRUE);

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

        userBillingAccountService.updateSubscriptionStatus(customerId, Boolean.FALSE);

        log.warn("⚠️ Invoice payment failed for customer: {}, amount: {}", customerId, amount);

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

    // Get subscriptionId directly from Invoice
//    private String getSubscriptionIdFromInvoice(Invoice invoice) {
//        try {
//            // Try to get the field using getField (if available in your SDK version)
//            Object subscriptionId = invoice.get("subscription");
//            if (subscriptionId instanceof String) {
//                return (String) subscriptionId;
//            }
//
//            // Fallback: parse raw JSON if needed
//            if (invoice.getRawJsonObject() != null) {
//                JsonObject raw = invoice.getRawJsonObject();
//                JsonElement subElem = raw.get("subscription");
//                if (subElem != null && subElem.isJsonPrimitive()) {
//                    return subElem.getAsString();
//                }
//            }
//
//            log.warn("\\u26A0\\uFE0F 'subscription' field is missing in Invoice.");
//        } catch (Exception e) {
//            log.error("\\u274C Failed to extract subscription ID from Invoice", e);
//        }
//        return null;
//    }

    private String getSubscriptionIdFromInvoice(Invoice invoice) {
        try {
            // Cách 2: Nếu không có, parse từ raw JSON
            JsonObject raw = invoice.getRawJsonObject();
            if (raw != null) {
                // Ưu tiên: lấy từ parent.subscription_details.subscription
                if (raw.has("parent")) {
                    JsonObject parent = raw.getAsJsonObject("parent");
                    if (parent.has("subscription_details")) {
                        JsonObject details = parent.getAsJsonObject("subscription_details");
                        if (details.has("subscription")) {
                            return details.get("subscription").getAsString();
                        }
                    }
                }

                // Fallback: lấy từ lines.data[0].parent.subscription_item_details.subscription
                if (raw.has("lines")) {
                    JsonObject lines = raw.getAsJsonObject("lines");
                    if (lines.has("data") && lines.get("data").isJsonArray()) {
                        JsonElement firstLine = lines.getAsJsonArray("data").get(0);
                        if (firstLine.isJsonObject()) {
                            JsonObject line = firstLine.getAsJsonObject();
                            if (line.has("parent")) {
                                JsonObject parent = line.getAsJsonObject("parent");
                                if (parent.has("subscription_item_details")) {
                                    JsonObject subDetails = parent.getAsJsonObject("subscription_item_details");
                                    if (subDetails.has("subscription")) {
                                        return subDetails.get("subscription").getAsString();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            log.warn("⚠️ Không tìm thấy subscriptionId trong Invoice (fallback thất bại).");
        } catch (Exception e) {
            log.error("❌ Lỗi khi lấy subscriptionId từ invoice", e);
        }
        return null;
    }

}
