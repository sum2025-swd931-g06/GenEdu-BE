package com.genedu.subscription.service;

import com.genedu.subscription.dto.WebhookRequest;
import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscription.SubscriptionResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    String startSubscription(SubscriptionRequestDTO requestDTO);
    void cancelAutoRenew(Object request);
    void notifyExpiringSubscriptions();
    List<SubscriptionResponseDTO> getUserSubscriptions(UUID userId);
    SubscriptionResponseDTO getActiveSubscription(UUID userId);
}
