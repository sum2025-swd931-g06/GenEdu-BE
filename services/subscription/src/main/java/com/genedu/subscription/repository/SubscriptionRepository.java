package com.genedu.subscription.repository;

import com.genedu.subscription.model.Subscription;
import com.genedu.subscription.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findAllByPlanAndAutoRenewTrueAndStatus(SubscriptionPlan plan, String status);
    Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);
//    List<Subscription> findAllByAutoRenewTrueAndEndedAtBetween(LocalDateTime start, LocalDateTime end);
@Query("SELECT s FROM Subscription s JOIN FETCH s.account JOIN FETCH s.plan " +
        "WHERE s.autoRenew = true AND s.renewalReminderSent = false AND s.endedAt BETWEEN :start AND :end")
List<Subscription> findAllByAutoRenewTrueAndEndedAtBetweenWithAccounts(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
);
}
