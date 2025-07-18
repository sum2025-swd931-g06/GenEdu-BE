package com.genedu.subscription.service;

import com.genedu.subscription.dto.WebhookRequest;
import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.dto.userbillingaccount.UserBillingAccountResponseDTO;
import com.genedu.subscription.model.SubscriptionPlan;
import com.genedu.subscription.model.UserBillingAccount;
import com.stripe.exception.StripeException;

import java.util.Map;

public interface PaymentGatewayService {
    Map<String, Object> createCheckoutSession(UserBillingAccountResponseDTO billing, SubscriptionPlanResponseDTO subscriptionPlan) throws StripeException;
    void cancelSubscription(String gatewaySubscriptionId);
    void handleWebhookEvent(WebhookRequest webhookRequest);
    boolean isWebhookSignatureValid(WebhookRequest webhookRequest);
}
