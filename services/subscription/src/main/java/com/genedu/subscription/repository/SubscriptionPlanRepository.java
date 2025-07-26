package com.genedu.subscription.repository;

import com.genedu.subscription.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, UUID>, JpaSpecificationExecutor<SubscriptionPlan> {

    boolean existsByPlanNameAndDeletedIsFalse(String planName);
    boolean existsByPlanNameAndDeletedIsFalseAndIdNot(String planeName, UUID planeId);
    List<SubscriptionPlan> findAllByDeletedIsFalse();
    Optional<SubscriptionPlan> findByIdAndDeletedIsFalse(UUID id);
    Optional<SubscriptionPlan> findByStripeProductIdAndDeletedIsFalse(String stripeProductId);

    List<SubscriptionPlan> findAllByDeletedIsFalseAndIsActiveIsTrue();
}
