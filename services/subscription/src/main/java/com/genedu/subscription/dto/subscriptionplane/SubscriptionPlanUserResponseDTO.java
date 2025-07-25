package com.genedu.subscription.dto.subscriptionplane;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record SubscriptionPlanUserResponseDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer durationInDays
) {
}