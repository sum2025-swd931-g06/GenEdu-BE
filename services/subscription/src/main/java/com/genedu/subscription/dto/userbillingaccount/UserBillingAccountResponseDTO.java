package com.genedu.subscription.dto.userbillingaccount;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserBillingAccountResponseDTO(
        UUID customerId,
        UUID userId,
        String paymentGatewayCustomerId,
        Boolean subscriptionStatus
) {
}
