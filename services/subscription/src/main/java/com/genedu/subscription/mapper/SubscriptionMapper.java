package com.genedu.subscription.mapper;

import com.genedu.subscription.dto.subscription.SubscriptionResponseDTO;
import com.genedu.subscription.model.Subscription;

public class SubscriptionMapper {
    public static SubscriptionResponseDTO toDTO(Subscription subscription) {
        return new SubscriptionResponseDTO(
                subscription.getId(),
                subscription.getStripeSubscriptionId(),
                subscription.getAccount().getPaymentGatewayCustomerId(),
                subscription.getPlan().getPlanName(),
                subscription.getStartedAt(),
                subscription.getEndedAt(),
                subscription.getAutoRenew(),
                subscription.getStatus()
        );
    }
}
