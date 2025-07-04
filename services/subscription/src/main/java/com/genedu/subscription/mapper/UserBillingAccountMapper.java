package com.genedu.subscription.mapper;

import com.genedu.subscription.dto.userbillingaccount.UserBillingAccountResponseDTO;
import com.genedu.subscription.model.UserBillingAccount;

public class UserBillingAccountMapper {
    public static UserBillingAccountResponseDTO toDTO(UserBillingAccount userBillingAccount) {
        return UserBillingAccountResponseDTO.builder()
                .customerId(userBillingAccount.getId())
                .userId(userBillingAccount.getUserId())
                .paymentGatewayCustomerId(userBillingAccount.getPaymentGatewayCustomerId())
                .subscriptionStatus(userBillingAccount.getSubscriptionStatus())
                .build();
    }
}
