package com.genedu.subscription.controller;

import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.service.SubscriptionPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provides endpoints for retrieving subscription plans available to end-users.
 * <p>
 * This controller is intended for public access, allowing users to view
 * available subscription plans.
 * Administrative operations are handled in {@link SubscriptionPlanManagerController}.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions/subscription-plans")
@Tag(name = "Subscription Plan", description = "Subscription Plan API")
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping
    @Operation(summary = "Lấy danh sách gói subscription hoặc chi tiết nếu có truyền planId")
    public ResponseEntity<?> getPlans(@RequestParam(required = false) String planId) {
        if (planId != null) {
            return subscriptionPlanService.getSubscriptionPlanForUser(planId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.ok(subscriptionPlanService.getAllSubscriptionPlansForUser());
    }


}
