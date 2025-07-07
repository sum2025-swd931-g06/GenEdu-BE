package com.genedu.subscription.service;

import com.genedu.subscription.dto.userbillingaccount.UserBillingAccountResponseDTO;
import com.genedu.subscription.model.UserBillingAccount;

import java.util.Optional;
import java.util.UUID;

public interface UserBillingAccountService {
    UserBillingAccountResponseDTO getOrCreateUserBillingAccount(UUID userId);
    Optional<UserBillingAccountResponseDTO> findByUserId(UUID userId);
    Optional<UserBillingAccountResponseDTO> findByPaymentGatewayCustomerId(UUID userId);
    void updatePaymentGatewayCustomerId(UUID userId, String customerId);
}
