package com.genedu.subscription.mapper;

import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.model.SubscriptionPlan;

import java.util.UUID;

public class SubscriptionPlanMapper {
    public static SubscriptionPlan toEntity(SubscriptionPlanRequestDTO requestDTO) {
        SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
        subscriptionPlan.setId(UUID.randomUUID());
        subscriptionPlan.setPlanName(requestDTO.name());
        subscriptionPlan.setDescription(requestDTO.description());
        subscriptionPlan.setPrice(requestDTO.price());
        subscriptionPlan.setDuration(requestDTO.durationInDays());
        return subscriptionPlan;
    }

    public static SubscriptionPlanResponseDTO toDTO(SubscriptionPlan subscriptionPlan) {
        return SubscriptionPlanResponseDTO.builder()
                .id(subscriptionPlan.getId())
                .name(subscriptionPlan.getPlanName())
                .description(subscriptionPlan.getDescription())
                .price(subscriptionPlan.getPrice())
                .durationInDays(subscriptionPlan.getDuration())
                .isDeleted(subscriptionPlan.isDeleted())
                .createdOn(subscriptionPlan.getCreatedOn())
                .createdBy(subscriptionPlan.getCreatedBy())
                .lastModifiedOn(subscriptionPlan.getLastModifiedOn())
                .lastModifiedBy(subscriptionPlan.getLastModifiedBy())
                .stripeProductId(subscriptionPlan.getStripeProductId())
                .stripePriceId(subscriptionPlan.getStripePriceId())
                .build();
    }
}
