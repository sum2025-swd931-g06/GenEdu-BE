package com.genedu.subscription.service.impl;

import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscription.SubscriptionResponseDTO;
import com.genedu.subscription.model.Subscription;
import com.genedu.subscription.repository.SubscriptionPlanRepository;
import com.genedu.subscription.repository.SubscriptionRepository;
import com.genedu.subscription.repository.UserBillingAccountRepository;
import com.genedu.subscription.service.EmailService;
import com.genedu.subscription.service.SubscriptionService;
import com.genedu.subscription.utils.Constants;
import com.stripe.exception.StripeException;
import com.stripe.param.SubscriptionUpdateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    @Value("${zone.id}")
    private String zoneId;

    private final UserBillingAccountRepository billingRepo;
    private final SubscriptionRepository subscriptionRepo;
    private final EmailService emailService;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public void startSubscription(SubscriptionRequestDTO requestDTO) {
        // Validate request
        if (requestDTO == null || requestDTO.accountId() == null || requestDTO.planId() == null) {
            throw new IllegalArgumentException("Invalid subscription request");
        }

        // Create or retrieve billing account
        var billingAccount = billingRepo.findByPaymentGatewayCustomerId(requestDTO.accountId())
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.CUSTOMER_NOT_FOUND, requestDTO.accountId()));

        var subscriptionPlan = subscriptionPlanRepository.findByStripeProductIdAndDeletedIsFalse(requestDTO.planId())
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND, requestDTO.planId()));


        try {
            ZoneId saigonZone = ZoneId.of(zoneId);

            // Create subscription entity in our database
            Subscription subscription = new Subscription();
            subscription.setId(UUID.randomUUID());
            subscription.setAccount(billingAccount);
            subscription.setPlan(subscriptionPlan);
            subscription.setStripeSubscriptionId(requestDTO.stripeSubscriptionId());
            subscription.setStartedAt(ZonedDateTime.now(saigonZone).toLocalDateTime());
            subscription.setEndedAt(ZonedDateTime.now(saigonZone).toLocalDateTime().plus(java.time.Duration.ofDays(subscriptionPlan.getDuration())));
            subscription.setAutoRenew(requestDTO.autoRenew());
            subscription.setStatus(requestDTO.status());
            subscription.setCreatedAt(ZonedDateTime.now(saigonZone).toLocalDateTime());

            // Save to database
            subscription = subscriptionRepo.save(subscription);

            // Send confirmation email if needed
            if (billingAccount.getUserId() != null) {
                emailService.sendConfirmationEmail(
                        requestDTO.toEmail(),
                        requestDTO.userName(),
                        subscriptionPlan.getPlanName(),
                        subscription.getEndedAt().toString(),
                        subscriptionPlan.getPrice().toString()
                );
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to create subscription", e);
        }
    }

    @Override
    public void cancelAutoRenew(String subscriptionId) throws StripeException {
        Subscription subscription = subscriptionRepo.findByStripeSubscriptionId(subscriptionId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.SUBSCRIPTION_NOT_FOUND, subscriptionId));
        com.stripe.model.Subscription stripeSubscription = com.stripe.model.Subscription.retrieve(subscriptionId);

        SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                .setCancelAtPeriodEnd(true)
                .build();

        stripeSubscription.update(params);

        subscription.setAutoRenew(false);
        subscription.setStatus(stripeSubscription.getStatus());
        subscription.setUpdatedAt(ZonedDateTime.now(ZoneId.of(zoneId)).toLocalDateTime());

        subscriptionRepo.save(subscription);
    }

    @Override
    public void turnOffAutoRenew(String subscriptionId, String status) throws StripeException {
        Subscription subscription = subscriptionRepo.findByStripeSubscriptionId(subscriptionId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.SUBSCRIPTION_NOT_FOUND, subscriptionId));

        if (subscription.getStatus().equalsIgnoreCase("active")) {
            subscription.setAutoRenew(false);
            subscription.setStatus(status);
            subscription.setUpdatedAt(ZonedDateTime.now(ZoneId.of(zoneId)).toLocalDateTime());

            subscriptionRepo.save(subscription);
        }
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
