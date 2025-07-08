package com.genedu.subscription.service.impl;

import com.genedu.subscription.dto.WebhookRequest;
import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.model.UserBillingAccount;
import com.genedu.subscription.service.PaymentGatewayService;

public class StripeService implements PaymentGatewayService {
    @Override
    public String createCheckoutSession(SubscriptionRequestDTO subscriptionRequestDTO, UserBillingAccount billing) {
        return "";
    }

    @Override
    public void cancelSubscription(String gatewaySubscriptionId) {

    }

    @Override
    public void handleWebhookEvent(WebhookRequest webhookRequest) {

    }

    @Override
    public boolean isWebhookSignatureValid(WebhookRequest webhookRequest) {
        return false;
    }
}
