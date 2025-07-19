package com.genedu.subscription.repository;

import com.genedu.subscription.model.UserBillingAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserBillingAccountRepository extends JpaRepository<UserBillingAccount, UUID> {
    Optional<UserBillingAccount> findByUserId(UUID userId);
    Optional<UserBillingAccount> findByPaymentGatewayCustomerId(String customerId);
}
