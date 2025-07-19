package com.genedu.subscription.dto.subscription;

public record SubscriptionRequestDTO(
        String accountId,
        String planId,
        Boolean autoRenew,
        String status
) {
    public SubscriptionRequestDTO{
        autoRenew = true;
    }
}
