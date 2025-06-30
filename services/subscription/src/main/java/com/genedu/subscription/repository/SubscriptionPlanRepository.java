package com.genedu.subscription.repository;

import com.genedu.subscription.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, UUID> {

}
