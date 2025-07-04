package com.genedu.subscription.service;

import com.genedu.subscription.dto.userbillingaccount.UserBillingAccountResponseDTO;
import com.genedu.subscription.model.UserBillingAccount;

import java.util.Optional;
import java.util.UUID;

public interface UserBillingAccountService {
    UserBillingAccountResponseDTO getOrCreateUserBillingAccount(String userId);
    Optional<UserBillingAccountResponseDTO> findByUserId(String userId);
//    Optional<UserBillingAccountResponseDTO> findByPaymentGatewayCustomerId(String userId);
    void updatePaymentGatewayCustomerId(String userId, String customerId);
    void updateSubscriptionStatus(String userId, Boolean status);
}
