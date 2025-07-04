package com.genedu.subscription.service.impl;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.DuplicatedException;
import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.mapper.SubscriptionPlanMapper;
import com.genedu.subscription.model.Subscription;
import com.genedu.subscription.model.SubscriptionPlan;
import com.genedu.subscription.repository.SubscriptionPlanRepository;
import com.genedu.subscription.repository.SubscriptionRepository;
import com.genedu.subscription.service.SubscriptionPlanService;
import com.genedu.subscription.utils.Constants;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public SubscriptionPlanResponseDTO createSubscriptionPlan(SubscriptionPlanRequestDTO requestDTO) {

        if (requestDTO.name() == null || requestDTO.name().isEmpty()) {
            throw new BadRequestException(Constants.ErrorCode.INVALID_SUBSCRIPTION_PLAN_NAME);
        }
        if (subscriptionPlanRepository.existsByPlanNameAndDeletedIsFalse(requestDTO.name())) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_SUBSCRIPTION_PLAN_NAME, requestDTO.name());
        }
        isValidPriceAndDuration(requestDTO);

        try {

            //1. Create a new Product in Stripe
            ProductCreateParams productParams = ProductCreateParams.builder()
                    .setName(requestDTO.name())
                    .setDescription(requestDTO.description())
                    .build();
            Product product = Product.create(productParams);

            //2. Create Price for the Product
            PriceCreateParams.Recurring recurring = PriceCreateParams.Recurring.builder()
                    .setInterval(PriceCreateParams.Recurring.Interval.DAY)
                    .setIntervalCount(requestDTO.durationInDays().longValue()) // Duration in days
                    .build();

            PriceCreateParams priceParams = PriceCreateParams.builder()
                    .setProduct(product.getId())
                    .setUnitAmount(requestDTO.price().longValue()) // Convert to smallest currency unit
                    .setCurrency("VND")
                    .setRecurring(recurring)
                    .build();

            Price price = Price.create(priceParams);

            // 3. Save into database

            SubscriptionPlan subscriptionPlan = SubscriptionPlanMapper.toEntity(requestDTO);
            subscriptionPlan.setStripeProductId(product.getId());
            subscriptionPlan.setStripePriceId(price.getId());

            SubscriptionPlan savedPlan = subscriptionPlanRepository.save(subscriptionPlan);
            return SubscriptionPlanMapper.toDTO(savedPlan);
        } catch (Exception e) {
            log.error("Error creating chapter", e);
            throw new InternalServerErrorException(Constants.ErrorCode.CREATE_FAILED, e.getMessage());
        }

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
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findByIdAndDeletedIsFalse(planUUID)
                .orElseThrow(() -> new BadRequestException(Constants.ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND, planId));

        if (subscriptionPlanRepository.existsByPlanNameAndDeletedIsFalseAndIdNot(requestDTO.name(), planUUID)) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_SUBSCRIPTION_PLAN_NAME, requestDTO.name());
        }

        isValidPriceAndDuration(requestDTO);

        try {
            updateStripeProductAndPrice(subscriptionPlan, requestDTO);

            SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(subscriptionPlan);


            return SubscriptionPlanMapper.toDTO(updatedPlan);
        } catch (Exception e) {
            log.error("Error creating chapter", e);
            throw new InternalServerErrorException(Constants.ErrorCode.CREATE_FAILED, e.getMessage());
        }
    }

    private void updateStripeProductAndPrice(SubscriptionPlan oldPlan, SubscriptionPlanRequestDTO requestDTO) {
        try {
            boolean priceChanged = !oldPlan.getPrice().equals(requestDTO.price());
            boolean productChanged = !oldPlan.getPlanName().equals(requestDTO.name()) ||
                    !oldPlan.getDescription().equals(requestDTO.description()) ||
                    !oldPlan.getDuration().equals(requestDTO.durationInDays());

            // Update Product in Stripe
            if (productChanged) {
                Product product = Product.retrieve(oldPlan.getStripeProductId());
                Map<String, Object> updateParams = new HashMap<>();
                updateParams.put("name", requestDTO.name());
                updateParams.put("description", requestDTO.description());
                product.update(updateParams);

                // Update local plan details
                oldPlan.setPlanName(requestDTO.name());
                oldPlan.setDescription(requestDTO.description());
            }
            if(priceChanged){
                PriceCreateParams.Recurring recurring = PriceCreateParams.Recurring.builder()
                        .setInterval(PriceCreateParams.Recurring.Interval.DAY)
                        .setIntervalCount(requestDTO.durationInDays().longValue()) // Duration in days
                        .build();

                PriceCreateParams priceParams = PriceCreateParams.builder()
                        .setProduct(oldPlan.getStripeProductId())
                        .setUnitAmount(requestDTO.price().longValue()) // Convert to smallest currency unit
                        .setCurrency("VND")
                        .setRecurring(recurring)
                        .build();
                Price price = Price.create(priceParams);

                // Update local plan details
                oldPlan.setStripePriceId(price.getId());
                oldPlan.setPrice(requestDTO.price());
                oldPlan.setDuration(requestDTO.durationInDays());
            }

        } catch (Exception e) {
            log.error("Error updating Stripe product or price", e);
            throw new InternalServerErrorException(Constants.ErrorCode.UPDATE_FAILED, e.getMessage());
        }
    }

    private void migrateSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        List<Subscription> activeSubs = subscriptionRepository.findAllByPlanAndAutoRenewTrueAndStatus(subscriptionPlan, "ACTIVE");
        for(Subscription sub : activeSubs) {
            String stripeSubId = sub.get;
            Subscription stripeSub = Subscription.retrieve(stripeSubId);
        }
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
                .orElseThrow(() -> new BadRequestException(Constants.ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND, planId));
    }

    @Override
    public void deleteSubscriptionPlan(String planId) {
        UUID planUUID = UUID.fromString(planId);
        SubscriptionPlan plan = subscriptionPlanRepository.findById(planUUID)
                .orElseThrow(() -> new BadRequestException(Constants.ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND, planId));
        try{
            plan.setDeleted(true);
            subscriptionPlanRepository.save(plan);
        } catch (Exception e) {
            log.error("Error deleting subscription plan", e);
            throw new InternalServerErrorException(Constants.ErrorCode.DELETE_FAILED, e.getMessage());
        }
    }

    @Override
    public List<SubscriptionPlanResponseDTO> getAllSubscriptionPlans() {
        return subscriptionPlanRepository.findAllByDeletedIsFalse()
                .stream()
                .map(SubscriptionPlanMapper::toDTO)
                .collect(Collectors.toList());
    }
}
