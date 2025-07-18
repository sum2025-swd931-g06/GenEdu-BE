package com.genedu.subscription.dto.userbillingaccount;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserBillingAccountResponseDTO(
        UUID id,
        UUID customerId,
        UUID userId,
        String paymentGatewayCustomerId,
        Boolean subscriptionStatus
) {
}
