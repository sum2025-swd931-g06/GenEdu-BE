package com.genedu.subscription.service.impl;

import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.subscription.dto.userbillingaccount.UserBillingAccountResponseDTO;
import com.genedu.subscription.mapper.UserBillingAccountMapper;
import com.genedu.subscription.model.UserBillingAccount;
import com.genedu.subscription.repository.UserBillingAccountRepository;
import com.genedu.subscription.service.UserBillingAccountService;
import com.genedu.subscription.utils.Constants;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserBillingAccountServiceImpl implements UserBillingAccountService {
    private final UserBillingAccountRepository userBillingAccountRepository;

    @Override
    public UserBillingAccountResponseDTO getOrCreateUserBillingAccount(String userId) {
        try {
            UUID userUUID = UUID.fromString(userId);
            var optional = userBillingAccountRepository.findByUserId(userUUID);
            UserBillingAccount account = optional.orElseGet(() -> UserBillingAccount.builder()
                    .id(UUID.randomUUID())
                    .userId(userUUID)
                    .paymentGatewayCustomerId("")
                    .subscriptionStatus(false)
                    .build());
            if (optional.isEmpty()) {
                userBillingAccountRepository.save(account);
            }
            return UserBillingAccountMapper.toDTO(account);
        } catch (Exception e) {
            log.error("Error when getOrCreateUserBillingAccount for userId {}", userId, e);
            throw new InternalServerErrorException("error.internal.server");
        }
    }

    @Override
    public Optional<UserBillingAccountResponseDTO> findByUserId(String userId) {
        try {
            UUID userUUID = UUID.fromString(userId);
            return userBillingAccountRepository.findByUserId(userUUID)
                    .map(UserBillingAccountMapper::toDTO);
        } catch (Exception e) {
            log.error("Error when findByUserId for userId {}", userId, e);
            throw new InternalServerErrorException("error.internal.server");
        }
    }

//    @Override
//    public Optional<UserBillingAccountResponseDTO> findByPaymentGatewayCustomerId(String customerId) {
//        try {
//            return userBillingAccountRepository.findByPaymentGatewayCustomerId(customerId)
//                    .map(UserBillingAccountMapper::toDTO);
//        } catch (Exception e) {
//            log.error("Error when findByPaymentGatewayCustomerId for customerId {}", customerId, e);
//            throw new InternalServerErrorException("error.internal.server");
//        }
//    }

    @Override
    public void updatePaymentGatewayCustomerId(String userId, String customerId) {
        UUID userUUID = UUID.fromString(userId);
        UserBillingAccount account = userBillingAccountRepository.findById(userUUID)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.USER_BILLING_ACCOUNT_NOT_FOUND, userId));
        try {
            account.setPaymentGatewayCustomerId(customerId);
            userBillingAccountRepository.save(account);
        } catch (Exception e) {
            log.error("Error when updatePaymentGatewayCustomerId for userId {}", userId, e);
            throw new InternalServerErrorException(Constants.ErrorCode.UPDATE_FAILED, "UserBillingAccount", userId);
        }
    }

    @Override
    public void updateSubscriptionStatus(String userId, Boolean status) {
        try {
            UUID userUUID = UUID.fromString(userId);
            UserBillingAccount account = userBillingAccountRepository.findByUserId(userUUID)
                    .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.USER_BILLING_ACCOUNT_NOT_FOUND, userId));
            account.setSubscriptionStatus(status);
            userBillingAccountRepository.save(account);
        } catch (Exception e) {
            log.error("Error when updateSubscriptionStatus for userId {}", userId, e);
            throw new InternalServerErrorException(Constants.ErrorCode.UPDATE_FAILED, "UserBillingAccount", userId);
        }
    }

    @Override
    public UserBillingAccountResponseDTO ensureStripeCustomer(String userId, String country) throws StripeException {
        var billingAccount = getOrCreateUserBillingAccount(userId);
        if (billingAccount.paymentGatewayCustomerId() == null || billingAccount.paymentGatewayCustomerId().isBlank()) {
            var stripeCustomer = Customer.create(CustomerCreateParams.builder()
                    .setAddress(CustomerCreateParams.Address.builder().setCountry(country).build())
                    .setMetadata(Map.of("userId", userId))
                    .build());
            updatePaymentGatewayCustomerId(billingAccount.id().toString(), stripeCustomer.getId());
            // Reload updated billing account
            billingAccount = getOrCreateUserBillingAccount(userId);
        }
        return billingAccount;
    }

    @Override
    public UserBillingAccount findByStripeCustomerId(String customerId) {
        try {
            return userBillingAccountRepository.findByPaymentGatewayCustomerId(customerId)
                    .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.USER_BILLING_ACCOUNT_NOT_FOUND, customerId));
        } catch (Exception e) {
            log.error("Error when findByStripeCustomerId for customerId {}", customerId, e);
            throw new InternalServerErrorException(Constants.ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}