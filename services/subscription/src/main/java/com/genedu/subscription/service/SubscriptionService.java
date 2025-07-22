package com.genedu.subscription.service;

import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscription.SubscriptionResponseDTO;
import com.stripe.exception.StripeException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionService {
    void startSubscription(SubscriptionRequestDTO requestDTO);
    void cancelAutoRenew(String subscriptionId) throws StripeException;
    void turnOffAutoRenew(String subscriptionId, String status) throws StripeException;
    void notifyExpiringSubscriptions();
    Optional<SubscriptionResponseDTO> getSubscriptionByStripeSubscriptionId(String stripeSubscriptionId);
    List<SubscriptionResponseDTO> getUserSubscriptions(UUID userId);
    SubscriptionResponseDTO getActiveSubscription(UUID userId);
}
