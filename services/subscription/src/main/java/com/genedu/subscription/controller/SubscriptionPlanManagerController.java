package com.genedu.subscription.controller;

import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.service.SubscriptionPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;

/**
 * Controller for managing subscription plans in the administrative context.
 * <p>
 * - Allows creation, updating, and soft deletion of plans.<br>
 * - Supports retrieval of all plans including those marked as deleted.<br>
 * - All actions are logged for auditing and compliance tracking.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions/manager/subscription-plans")
@Tag(name = "Subscription Plan Manager", description = "APIs for managing subscription plans (admin only)")
public class SubscriptionPlanManagerController {

    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping
    @Operation(summary = "Create a new subscription plan")
    public ResponseEntity<SubscriptionPlanResponseDTO> create(@RequestBody SubscriptionPlanRequestDTO requestDTO) {
        SubscriptionPlanResponseDTO created = subscriptionPlanService.createSubscriptionPlan(requestDTO);
        return ResponseEntity.created(URI.create("/api/v1/subscriptions/manager/subscription-plans/" + created.id()))
                .body(created);
    }

    @PutMapping("/{planId}")
    @Operation(summary = "Update an existing subscription plan")
    public ResponseEntity<SubscriptionPlanResponseDTO> update(@PathVariable String planId,
                                                              @RequestBody SubscriptionPlanRequestDTO requestDTO) {
        return ResponseEntity.ok(subscriptionPlanService.updateSubscriptionPlan(planId, requestDTO));
    }

    @DeleteMapping("/{planId}")
    @Operation(summary = "Delete a subscription plan (soft delete)")
    public ResponseEntity<Void> delete(@PathVariable String planId) {
        subscriptionPlanService.deleteSubscriptionPlan(planId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{planId}")
    @Operation(summary = "Get subscription plan details by ID")
    public ResponseEntity<SubscriptionPlanResponseDTO> getById(@PathVariable String planId) {
        return subscriptionPlanService.getSubscriptionPlan(planId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//
//    @GetMapping
//    @Operation(summary = "Get a list of all subscription plans (including soft-deleted ones)")
//    public ResponseEntity<List<SubscriptionPlanResponseDTO>> getAll() {
//        return ResponseEntity.ok(subscriptionPlanService.getAllSubscriptionPlansNotDeletedAndActive());
//    }

    @GetMapping("/search")
    @Operation(summary = "Search for subscription plans with optional filters")
    public ResponseEntity<Page<SubscriptionPlanResponseDTO>> searchPlans(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Integer durationInDays,
            @RequestParam(required = false) BigDecimal price,
            @ParameterObject Pageable pageable
    ) {
        Page<SubscriptionPlanResponseDTO> result = subscriptionPlanService.searchPlans(name, isActive, durationInDays, price, pageable);
        return ResponseEntity.ok(result);
    }
}