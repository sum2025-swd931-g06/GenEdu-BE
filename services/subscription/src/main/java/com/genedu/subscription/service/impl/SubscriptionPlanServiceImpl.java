package com.genedu.subscription.service.impl;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.DuplicatedException;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.mapper.SubscriptionPlanMapper;
import com.genedu.subscription.model.SubscriptionPlan;
import com.genedu.subscription.repository.SubscriptionPlanRepository;
import com.genedu.subscription.service.SubscriptionPlanService;
import com.genedu.subscription.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    @Transactional
    public SubscriptionPlanResponseDTO createSubscriptionPlan(SubscriptionPlanRequestDTO requestDTO) {
        if(subscriptionPlanRepository.existsByPlanNameAndDeletedIsFalse(requestDTO.name())) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_SUBSCRIPTION_PLAN_NAME);
        }
        isValidPriceAndDuration(requestDTO);

        SubscriptionPlan subscriptionPlan = SubscriptionPlanMapper.toEntity(requestDTO);

        SubscriptionPlan savedPlan = subscriptionPlanRepository.save(subscriptionPlan);
        return SubscriptionPlanMapper.toDTO(savedPlan);
    }

    private void isValidPriceAndDuration(SubscriptionPlanRequestDTO requestDTO) {
        if (requestDTO.price() == null || requestDTO.price().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException(Constants.ErrorCode.INVALID_SUBSCRIPTION_PLAN_PRICE);
        }
        if (requestDTO.durationInDays() == null || requestDTO.durationInDays() <= 0) {
            throw new BadRequestException(Constants.ErrorCode.INVALID_SUBSCRIPTION_PLAN_DURATION);
        }
    }

    @Override
    public SubscriptionPlanResponseDTO updateSubscriptionPlan(String planId, SubscriptionPlanRequestDTO requestDTO) {
        UUID planUUID = UUID.fromString(planId);
        if (!subscriptionPlanRepository.existsById(planUUID)) {
            throw new BadRequestException(Constants.ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND);
        }
        if (subscriptionPlanRepository.existsByPlanNameAndDeletedIsFalseAndIdNot(requestDTO.name(), planUUID)) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_SUBSCRIPTION_PLAN_NAME);
        }
        isValidPriceAndDuration(requestDTO);
        SubscriptionPlan subscriptionPlan = SubscriptionPlanMapper.toEntity(requestDTO);

        SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(subscriptionPlan);
        return SubscriptionPlanMapper.toDTO(updatedPlan);
    }

    @Override
    public Optional<SubscriptionPlanResponseDTO> getSubscriptionPlan(String planId) {
        UUID planUUID = UUID.fromString(planId);
        return subscriptionPlanRepository.findById(planUUID)
                .filter(p -> !p.isDeleted())
                .map(SubscriptionPlanMapper::toDTO);
    }

    @Override
    public SubscriptionPlan getSubscriptionPlanEntity(String planId) {
        UUID planUUID = UUID.fromString(planId);
        return subscriptionPlanRepository.findByIdAndDeletedIsFalse(planUUID)
                .orElseThrow(() -> new BadRequestException(Constants.ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));
    }

    @Override
    public void deleteSubscriptionPlan(String planId) {
        UUID planUUID = UUID.fromString(planId);
        SubscriptionPlan plan = subscriptionPlanRepository.findById(planUUID)
                .orElseThrow(() -> new BadRequestException(Constants.ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND));
        plan.setDeleted(true);
        subscriptionPlanRepository.save(plan);
    }

    @Override
    public List<SubscriptionPlanResponseDTO> getAllSubscriptionPlans() {
        return subscriptionPlanRepository.findAllByDeletedIsFalse()
                .stream()
                .map(SubscriptionPlanMapper::toDTO)
                .collect(Collectors.toList());
    }
}
