package com.genedu.subscription.service.impl;

import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscription.SubscriptionResponseDTO;
import com.genedu.subscription.model.Subscription;
import com.genedu.subscription.model.UserBillingAccount;
import com.genedu.subscription.repository.SubscriptionPlanRepository;
import com.genedu.subscription.repository.SubscriptionRepository;
import com.genedu.subscription.repository.UserBillingAccountRepository;
import com.genedu.subscription.repository.UserTransactionRepository;
import com.genedu.subscription.service.EmailService;
import com.genedu.subscription.service.SubscriptionService;
import com.genedu.subscription.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserBillingAccountRepository billingRepo;
    private final SubscriptionRepository subscriptionRepo;
    private final EmailService emailService;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public String startSubscription(SubscriptionRequestDTO requestDTO) {
        // Validate request
        if (requestDTO == null || requestDTO.accountId() == null || requestDTO.planId() == null) {
            throw new IllegalArgumentException("Invalid subscription request");
        }

        // Create or retrieve billing account
        var billingAccount = billingRepo.findByPaymentGatewayCustomerId(requestDTO.accountId())
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.CUSTOMER_NOT_FOUND, requestDTO.accountId()));

        var subscriptionPlan = subscriptionPlanRepository.findById(UUID.fromString(requestDTO.planId()))
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND, requestDTO.planId()));


        try {
            // Create subscription entity in our database
            Subscription subscription = new Subscription();
            subscription.setAccount(billingAccount);
            subscription.setPlan(subscriptionPlan);
            subscription.setStartedAt(Instant.now());
            subscription.setEndedAt(Instant.now().plus(java.time.Duration.ofDays(subscriptionPlan.getDuration())));
            subscription.setAutoRenew(requestDTO.autoRenew());
            subscription.setStatus(requestDTO.status());
            subscription.setCreatedAt(Instant.now());

            // Save to database
            subscription = subscriptionRepo.save(subscription);

            // Send confirmation email if needed
            if (billingAccount.getUserId() != null) {
                emailService.sendConfirmationEmail(
                        "shinkiriloveforever@gmail.com",
                        "NganNganChimte",
                        subscriptionPlan.getPlanName(),
                        subscription.getEndedAt().toString(),
                        subscriptionPlan.getPrice().toString()
                );
            }

            return subscription.getId().toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create subscription", e);
        }
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
