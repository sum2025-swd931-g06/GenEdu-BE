package com.genedu.subscription.service.impl;

import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscription.SubscriptionResponseDTO;
import com.genedu.subscription.repository.SubscriptionRepository;
import com.genedu.subscription.repository.UserBillingAccountRepository;
import com.genedu.subscription.repository.UserTransactionRepository;
import com.genedu.subscription.service.EmailService;
import com.genedu.subscription.service.SubscriptionService;
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
        return null;
    }

    @Override
    public ResponseEntity<String> handleWebhook(String payload, String signature) {
        // Here you would typically process the webhook payload and signature
        // For now, we just return a dummy response
        return ResponseEntity.ok("Webhook handled successfully");
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
