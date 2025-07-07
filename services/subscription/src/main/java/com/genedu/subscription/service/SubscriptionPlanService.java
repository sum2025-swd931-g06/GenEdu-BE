package com.genedu.subscription.service;

import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.model.SubscriptionPlan;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanService {
    SubscriptionPlanResponseDTO createSubscriptionPlan(SubscriptionPlanRequestDTO requestDTO);
    SubscriptionPlanResponseDTO updateSubscriptionPlan(String planId, SubscriptionPlanRequestDTO requestDTO);
    Optional<SubscriptionPlanResponseDTO> getSubscriptionPlan(String planId);
    SubscriptionPlan getSubscriptionPlanEntity(String planId);
    void deleteSubscriptionPlan(String planId);
    List<SubscriptionPlanResponseDTO> getAllSubscriptionPlans();
}
