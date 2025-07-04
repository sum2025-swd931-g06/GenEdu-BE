package com.genedu.subscription.controller;

import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.service.SubscriptionPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/subscriptions/subscription-plans")
@Tag(name = "Subscription Plan Admin", description = "Subscription Plan Admin API")
public class SubscriptionPlanManagerController {

    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping
    public ResponseEntity<SubscriptionPlanResponseDTO> create(@RequestBody SubscriptionPlanRequestDTO requestDTO) {
        return ResponseEntity.ok(subscriptionPlanService.createSubscriptionPlan(requestDTO));
    }

    @PutMapping("/{planId}")
    public ResponseEntity<SubscriptionPlanResponseDTO> update(@PathVariable String planId, @RequestBody SubscriptionPlanRequestDTO requestDTO) {
        return ResponseEntity.ok(subscriptionPlanService.updateSubscriptionPlan(planId, requestDTO));
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> delete(@PathVariable String planId) {
        subscriptionPlanService.deleteSubscriptionPlan(planId);
        return ResponseEntity.noContent().build();
    }
}
