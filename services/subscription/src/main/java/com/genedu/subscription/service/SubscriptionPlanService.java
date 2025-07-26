package com.genedu.subscription.service;

import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanUserResponseDTO;
import com.genedu.subscription.model.SubscriptionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanService {
    //Manager Methods
    SubscriptionPlanResponseDTO createSubscriptionPlan(SubscriptionPlanRequestDTO requestDTO);
    SubscriptionPlanResponseDTO updateSubscriptionPlan(String planId, SubscriptionPlanRequestDTO requestDTO);
    void deleteSubscriptionPlan(String planId);
    Optional<SubscriptionPlanResponseDTO> getSubscriptionPlan(String planId);
    Page<SubscriptionPlanResponseDTO> searchPlans(String name, Boolean isActive, Integer durationInDays, BigDecimal price, Pageable pageable);
    SubscriptionPlan getSubscriptionPlanEntity(String planId);

    //User Methods
    List<SubscriptionPlanUserResponseDTO> getAllSubscriptionPlansForUser();
    Optional<SubscriptionPlanUserResponseDTO> getSubscriptionPlanForUser(String planId);
}
