package com.genedu.subscription.dto.subscription;

public record SubscriptionRequestDTO(
        String accountId,
        String planId,
        String stripeSubscriptionId,
        Boolean autoRenew,
        String status,
        String toEmail,
        String userName
) {
    public SubscriptionRequestDTO{
        autoRenew = true;
    }
}
