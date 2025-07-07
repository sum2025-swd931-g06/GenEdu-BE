package com.genedu.subscription.controller;

import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.service.SubscriptionPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "Subscription Plan", description = "Subscription Plan API")
public class SubscriptionPlanController {
    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping
    public ResponseEntity<SubscriptionPlanResponseDTO> create(@RequestBody SubscriptionPlanRequestDTO requestDTO) {
        return ResponseEntity.ok(subscriptionPlanService.createSubscriptionPlan(requestDTO));
    }

    @PutMapping("/subscription-plans/{planId}")
    public ResponseEntity<SubscriptionPlanResponseDTO> update(@PathVariable String planId, @RequestBody SubscriptionPlanRequestDTO requestDTO) {
        return ResponseEntity.ok(subscriptionPlanService.updateSubscriptionPlan(planId, requestDTO));
    }

    @GetMapping("/subscription-plans/{planId}")
    public ResponseEntity<SubscriptionPlanResponseDTO> get(@PathVariable String planId) {
        return subscriptionPlanService.getSubscriptionPlan(planId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/subscription-plans")
    public ResponseEntity<List<SubscriptionPlanResponseDTO>> getAll() {
        return ResponseEntity.ok(subscriptionPlanService.getAllSubscriptionPlans());
    }

    @DeleteMapping("/subscription-plans/{planId}")
    public ResponseEntity<Void> delete(@PathVariable String planId) {
        subscriptionPlanService.deleteSubscriptionPlan(planId);
        return ResponseEntity.noContent().build();
    }
}
