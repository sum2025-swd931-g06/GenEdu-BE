package com.genedu.subscription.service.impl;

import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscription.SubscriptionResponseDTO;
import com.genedu.subscription.repository.SubscriptionRepository;
import com.genedu.subscription.repository.UserBillingAccountRepository;
import com.genedu.subscription.repository.UserTransactionRepository;
import com.genedu.subscription.service.EmailService;
import com.genedu.subscription.service.SubscriptionService;
import com.genedu.subscription.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final StripeService stripeService;
    private final UserBillingAccountRepository billingRepo;
    private final SubscriptionRepository subscriptionRepo;
    private final UserTransactionRepository transactionRepo;
    private final EmailService emailService;

    @Override
    public String startSubscription(SubscriptionRequestDTO requestDTO) {
//        // Validate request
//        if (requestDTO == null || requestDTO.accountId() == null || requestDTO.planId() == null) {
//            throw new IllegalArgumentException("Invalid subscription request");
//        }
//
//        // Create or retrieve billing account
//        var billingAccount = billingRepo.findByPaymentGatewayCustomerId(requestDTO.accountId())
//                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.CUSTOMER_NOT_FOUND, requestDTO.accountId()));
//
//        // Create subscription in Stripe
//        String subscriptionId = stripeService.createSubscription(billingAccount, requestDTO.getPlanId());
//
//        // Save subscription details in the repository
//        SubscriptionResponseDTO subscriptionResponse = new SubscriptionResponseDTO();
//        subscriptionResponse.setUserId(requestDTO.getUserId());
//        subscriptionResponse.setSubscriptionId(subscriptionId);
//        subscriptionResponse.setPlanId(requestDTO.getPlanId());
//        subscriptionRepo.save(subscriptionResponse);
//
//        // Send confirmation email
//        emailService.sendSubscriptionConfirmationEmail(requestDTO.getUserId(), subscriptionResponse);
//
//        return subscriptionId;
        return null;
    }

    @Override
    public void cancelAutoRenew(Object request) {


    }

    @Override
    public void notifyExpiringSubscriptions() {

    }

    @Override
    public List<SubscriptionResponseDTO> getUserSubscriptions(UUID userId) {
        return List.of();
    }

    @Override
    public SubscriptionResponseDTO getActiveSubscription(UUID userId) {
        return null;
    }
}
