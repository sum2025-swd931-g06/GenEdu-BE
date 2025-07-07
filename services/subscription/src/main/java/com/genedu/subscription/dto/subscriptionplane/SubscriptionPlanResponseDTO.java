package com.genedu.subscription.dto.subscriptionplane;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record SubscriptionPlanResponseDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer durationInDays,
        ZonedDateTime createdOn,
        UUID createdBy,
        ZonedDateTime lastModifiedOn,
        UUID lastModifiedBy
) {
}
