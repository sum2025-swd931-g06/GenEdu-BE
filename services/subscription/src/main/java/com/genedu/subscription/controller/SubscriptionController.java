package com.genedu.subscription.controller;

import com.genedu.subscription.dto.subscription.SubscriptionRequestDTO;
import com.genedu.subscription.dto.subscription.SubscriptionResponseDTO;
import com.genedu.subscription.model.Subscription;
import com.genedu.subscription.repository.SubscriptionRepository;
import com.genedu.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "Subscription", description = "Subscription API")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final SubscriptionRepository subscriptionRepository;

    @Operation(summary = "Create a session url to start a subscription")
    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startSubscription(@RequestBody SubscriptionRequestDTO request) {
        String sessionUrl = subscriptionService.startSubscription(request);
        return ResponseEntity.ok(Map.of("url", sessionUrl));
    }

    @Operation(summary = "Handle Stripe webhook events")
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signature) {
        return subscriptionService.handleWebhook(payload, signature);
    }

    @Operation(summary = "Cancel auto-renewal for a subscription")
    @PostMapping("/cancel-auto-renew")
    public ResponseEntity<String> cancelAutoRenew(@RequestParam Object request) {
        subscriptionService.cancelAutoRenew(request);
        return ResponseEntity.ok("Auto-renew canceled");
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<SubscriptionResponseDTO>> getUserSubscriptions(@PathVariable String userId) {
        var result = subscriptionService.getUserSubscriptions(UUID.fromString(userId));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/{userId}/active")
    public ResponseEntity<SubscriptionResponseDTO> getActiveSubscription(@PathVariable String userId) {
        var result = subscriptionService.getActiveSubscription(UUID.fromString(userId));
        return ResponseEntity.ok(result);
    }

//    @Scheduled(cron = "0 0 9 * * ?") // chạy mỗi 9h sáng hàng ngày
//    public void notifyExpiringSubscriptions() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime threshold = now.plusDays(3); // hoặc cấu hình số ngày từ config
//
//        List<Subscription> expiring = subscriptionRepository.findAllByAutoRenewTrueAndEndedAtBetween(now, threshold);
//        for (Subscription sub : expiring) {
//            emailService.sendReminder(sub.getUserId(), "Your subscription will expire on " + sub.getEndedAt());
//        }
//    }


}
