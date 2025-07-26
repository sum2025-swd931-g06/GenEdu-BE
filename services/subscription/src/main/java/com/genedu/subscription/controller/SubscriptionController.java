package com.genedu.subscription.controller;

import com.genedu.subscription.dto.subscription.SubscriptionResponseDTO;
import com.genedu.subscription.service.SubscriptionService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "Subscription", description = "Subscription API")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Operation(summary = "Cancel auto-renewal for a subscription by ID")
    @PostMapping("/{subscriptionId}/cancel-auto-renew")
    public ResponseEntity<String> cancelAutoRenew(@PathVariable String subscriptionId) throws StripeException {
        subscriptionService.cancelAutoRenew(subscriptionId);
        return ResponseEntity.ok("Auto-renew canceled");
    }

        @Scheduled(cron = "0 0 9 * * ?")
//    @Scheduled(cron = "*/10 * * * * *")
    public ResponseEntity<Void> notifyExpiringSubscriptions() throws StripeException {
        subscriptionService.notifyExpiringSubscriptions();
        return ResponseEntity.noContent().build();
    }
}
