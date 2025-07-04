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
@RequestMapping("/api/v1/subscriptions/subscription-plans")
@Tag(name = "Subscription Plan", description = "Subscription Plan API")
public class SubscriptionPlanController {
    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping("/{planId}")
    public ResponseEntity<SubscriptionPlanResponseDTO> get(@PathVariable String planId) {
        return subscriptionPlanService.getSubscriptionPlan(planId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<List<SubscriptionPlanResponseDTO>> getAll() {
        return ResponseEntity.ok(subscriptionPlanService.getAllSubscriptionPlans());
    }
}
