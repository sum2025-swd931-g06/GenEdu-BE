package com.genedu.subscription.dto.subscriptionplane;

import java.math.BigDecimal;

public record SubscriptionPlanRequestDTO(
        String name,
        String description,
        BigDecimal price,
        Integer durationInDays
        ) {
}
