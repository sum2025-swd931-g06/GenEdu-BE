package com.genedu.subscription.dto.subscriptionplane;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record SubscriptionPlanResponseDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String stripePriceId,
        Integer durationInDays,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String stripeProductId,
        boolean isActive,
        ZonedDateTime createdOn,
        ZonedDateTime lastModifiedOn
) {
}
