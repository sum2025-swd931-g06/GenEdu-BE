package com.genedu.subscription.service;

import com.genedu.subscription.dto.WebhookRequest;
import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscription.SubscriptionResponseDTO;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    SubscriptionResponseDTO startSubscription(SubscriptionRequestDTO requestDTO);
    void cancelAutoRenew(String subscriptionId);
    void handleWebhook(WebhookRequest webhookRequest);
    void notifyExpiringSubscriptions();
    List<SubscriptionResponseDTO> getUserSubscriptions(UUID userId);
    SubscriptionResponseDTO getActiveSubscription(UUID userId);
}
