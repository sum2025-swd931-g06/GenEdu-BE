package com.genedu.subscription.service.impl;

import com.genedu.commonlibrary.exception.BadRequestException;
import com.genedu.commonlibrary.exception.DuplicatedException;
import com.genedu.commonlibrary.exception.InternalServerErrorException;
import com.genedu.commonlibrary.exception.NotFoundException;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanRequestDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanResponseDTO;
import com.genedu.subscription.dto.subscriptionplane.SubscriptionPlanUserResponseDTO;
import com.genedu.subscription.mapper.SubscriptionPlanMapper;
import com.genedu.subscription.model.SubscriptionPlan;
import com.genedu.subscription.repository.SubscriptionPlanRepository;
import com.genedu.subscription.service.SubscriptionPlanService;
import com.genedu.subscription.utils.Constants;
import com.genedu.subscription.utils.SubscriptionPlanSpecification;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for managing subscription plans.
 * Handles creation, update, soft deletion, and retrieval of plans.
 * Integrates with Stripe for product and pricing management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    /**
     * Creates a new subscription plan and registers it on Stripe as both a Product and Price.
     *
     * @param requestDTO DTO containing subscription plan details.
     * @return the created SubscriptionPlanResponseDTO.
     * @throws BadRequestException          if the input is invalid.
     * @throws DuplicatedException          if a plan with the same name already exists.
     * @throws InternalServerErrorException if creation fails due to Stripe or database issues.
     */
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
                    .setTaxCode("txcd_10000000") // txcd_10000000: General - Electronically Supplied Services
                    .setActive(requestDTO.isActive())
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
                    .setTaxBehavior(PriceCreateParams.TaxBehavior.INCLUSIVE)
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

    /**
     * Validates that the price and duration of the subscription plan are positive and not null.
     *
     * @param requestDTO the DTO containing subscription plan input data.
     * @throws BadRequestException if price or duration is invalid.
     */
    private void isValidPriceAndDuration(SubscriptionPlanRequestDTO requestDTO) {
        if (requestDTO.price() == null || requestDTO.price().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException(Constants.ErrorCode.INVALID_SUBSCRIPTION_PLAN_PRICE);
        }
        if (requestDTO.durationInDays() == null || requestDTO.durationInDays() <= 0) {
            throw new BadRequestException(Constants.ErrorCode.INVALID_SUBSCRIPTION_PLAN_DURATION);
        }
    }

    /**
     * Updates a subscription plan's information (name, description, duration, price).
     * If name/description/duration changes → updates the Stripe Product.
     * If price changes → creates a new Stripe Price and updates the local record.
     *
     * @param planId     the ID of the subscription plan to update.
     * @param requestDTO the new subscription plan data.
     * @return the updated SubscriptionPlanResponseDTO.
     * @throws BadRequestException          if the plan ID is invalid or not found.
     * @throws DuplicatedException          if a plan with the new name already exists.
     * @throws InternalServerErrorException if Stripe or DB operations fail.
     */
    @Override
    @Transactional
    public SubscriptionPlanResponseDTO updateSubscriptionPlan(String planId, SubscriptionPlanRequestDTO requestDTO) {
        UUID planUUID = UUID.fromString(planId);
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findByIdAndDeletedIsFalse(planUUID)
                .orElseThrow(() -> new BadRequestException(Constants.ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND, planId));

        if (subscriptionPlanRepository.existsByPlanNameAndDeletedIsFalseAndIdNot(requestDTO.name(), planUUID)) {
            throw new DuplicatedException(Constants.ErrorCode.DUPLICATED_SUBSCRIPTION_PLAN_NAME, requestDTO.name());
        }

        isValidPriceAndDuration(requestDTO);

        try {
            boolean priceChanged = !subscriptionPlan.getPrice().equals(requestDTO.price());
            boolean planInfoChanged =
                    !subscriptionPlan.getPlanName().equals(requestDTO.name()) ||
                            !subscriptionPlan.getDescription().equals(requestDTO.description()) ||
                            !subscriptionPlan.getDuration().equals(requestDTO.durationInDays()) ||
                            !subscriptionPlan.getIsActive().equals(requestDTO.isActive());

            if (planInfoChanged) {
                Product product = Product.retrieve(subscriptionPlan.getStripeProductId());

                Map<String, Object> updateParams = new HashMap<>();
                updateParams.put("name", requestDTO.name());
                updateParams.put("description", requestDTO.description());
                updateParams.put("active", requestDTO.isActive());
//                product.setActive(requestDTO.isActive());
                product.update(updateParams);

                subscriptionPlan.setPlanName(requestDTO.name());
                subscriptionPlan.setDescription(requestDTO.description());
                subscriptionPlan.setDuration(requestDTO.durationInDays());
                subscriptionPlan.setIsActive(requestDTO.isActive());
            }

            if (priceChanged) {
                PriceCreateParams.Recurring recurring = PriceCreateParams.Recurring.builder()
                        .setInterval(PriceCreateParams.Recurring.Interval.DAY)
                        .setIntervalCount(requestDTO.durationInDays().longValue())
                        .build();

                PriceCreateParams priceParams = PriceCreateParams.builder()
                        .setProduct(subscriptionPlan.getStripeProductId())
                        .setUnitAmount(requestDTO.price().longValue())
                        .setCurrency("VND")
                        .setRecurring(recurring)
                        .setTaxBehavior(PriceCreateParams.TaxBehavior.INCLUSIVE)
                        .build();

                Price newPrice = Price.create(priceParams); // <-- Nếu Stripe lỗi tại đây, sẽ rollback

                subscriptionPlan.setStripePriceId(newPrice.getId());
                subscriptionPlan.setPrice(requestDTO.price());
            }

            SubscriptionPlan savedPlan = subscriptionPlanRepository.save(subscriptionPlan);
            return SubscriptionPlanMapper.toDTO(savedPlan);

        } catch (com.stripe.exception.StripeException se) {
            log.error("Stripe error when updating plan: {}", se.getMessage(), se);
            throw new InternalServerErrorException(Constants.ErrorCode.STRIPE_API_FAILED, se.getMessage());
        } catch (Exception e) {
            log.error("Error updating subscription plan", e);
            throw new InternalServerErrorException(Constants.ErrorCode.UPDATE_FAILED, e.getMessage());
        }
    }

    /**
     * Retrieves a subscription plan by its ID (only if not marked as deleted).
     *
     * @param planId the string ID of the subscription plan.
     * @return an Optional containing the SubscriptionPlanResponseDTO if found and not deleted.
     * @throws BadRequestException if the plan ID is not a valid UUID format.
     */
    @Override
    public Optional<SubscriptionPlanResponseDTO> getSubscriptionPlan(String planId) {
        try {
            UUID planUUID = UUID.fromString(planId);
            return subscriptionPlanRepository.findByIdAndDeletedIsFalse(planUUID)
                    .map(SubscriptionPlanMapper::toDTO);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(Constants.ErrorCode.INVALID_SUBSCRIPTION_PLAN_ID, planId);
        } catch (Exception e) {
            log.error("Error retrieving subscription plan", e);
            throw new InternalServerErrorException(Constants.ErrorCode.RETRIEVE_FAILED, e.getMessage());
        }
    }

    @Override
    public Page<SubscriptionPlanResponseDTO> searchPlans(String name, Boolean isActive, Integer durationInDays, BigDecimal price, Pageable pageable) {
        Specification<SubscriptionPlan> spec = SubscriptionPlanSpecification.build(name, isActive, durationInDays, price);

        return subscriptionPlanRepository.findAll(spec, pageable)
                .map(SubscriptionPlanMapper::toDTO);
    }

    /**
     * Retrieves the SubscriptionPlan entity by ID (only if not marked as deleted).
     *
     * @param planId the string ID of the plan.
     * @return the SubscriptionPlan entity.
     * @throws BadRequestException if the plan is not found or deleted.
     */
    @Override
    public SubscriptionPlan getSubscriptionPlanEntity(String planId) {
        UUID planUUID = UUID.fromString(planId);
        return subscriptionPlanRepository.findByIdAndDeletedIsFalse(planUUID)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND, planId));
    }

    @Override
    public List<SubscriptionPlanUserResponseDTO> getAllSubscriptionPlansForUser() {
        return subscriptionPlanRepository.findAllByDeletedIsFalseAndIsActiveIsTrue().stream()
                .map(SubscriptionPlanMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SubscriptionPlanUserResponseDTO> getSubscriptionPlanForUser(String planId) {
        return Optional.empty();
    }

    /**
     * Soft-deletes a subscription plan and marks the corresponding Stripe Product as inactive.
     *
     * @param planId the ID of the subscription plan to delete.
     * @throws BadRequestException          if the plan is not found.
     * @throws InternalServerErrorException if the deletion fails or Stripe API fails.
     */
    @Override
    @Transactional
    public void deleteSubscriptionPlan(String planId) {
        UUID planUUID = UUID.fromString(planId);
        SubscriptionPlan plan = subscriptionPlanRepository.findById(planUUID)
                .orElseThrow(() -> new BadRequestException(Constants.ErrorCode.SUBSCRIPTION_PLAN_NOT_FOUND, planId));
        try {
            // 1. Ẩn Product trên Stripe (ẩn chứ không xóa vì Stripe không hỗ trợ xóa Product)
            Product product = Product.retrieve(plan.getStripeProductId());
            Map<String, Object> stripeParams = new HashMap<>();
            product.setActive(false);
            plan.setDeleted(true);
//            product.update(stripeParams);
            subscriptionPlanRepository.save(plan);
        } catch (Exception e) {
            log.error("Error deleting subscription plan", e);
            throw new InternalServerErrorException(Constants.ErrorCode.DELETE_FAILED, e.getMessage());
        }
    }
}
