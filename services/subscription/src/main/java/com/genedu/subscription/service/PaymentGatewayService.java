package com.genedu.subscription.service;

import com.genedu.subscription.dto.WebhookRequest;
import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.model.UserBillingAccount;

public interface PaymentGatewayService {
    String createCheckoutSession(SubscriptionRequestDTO subscriptionRequestDTO, UserBillingAccount billing);
    void cancelSubscription(String gatewaySubscriptionId);
    void handleWebhookEvent(WebhookRequest webhookRequest);
    boolean isWebhookSignatureValid(WebhookRequest webhookRequest);

}
