package com.genedu.subscription.dto.subscription;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubscriptionResponseDTO(
        UUID id,
        String stripeSubscriptionId,
        String stripeCustomerId,
        String planName,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        boolean isAutoRenew,
        String status
) {
}
