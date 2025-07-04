package com.genedu.subscription.repository;

import com.genedu.subscription.model.Subscription;
import com.genedu.subscription.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findAllByPlanAndAutoRenewTrueAndStatus(SubscriptionPlan plan, String status);
}
