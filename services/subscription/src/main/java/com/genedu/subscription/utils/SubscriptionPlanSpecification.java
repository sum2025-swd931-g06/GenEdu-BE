package com.genedu.subscription.utils;

import com.genedu.subscription.model.SubscriptionPlan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class SubscriptionPlanSpecification {
    public static Specification<SubscriptionPlan> build(String name, Boolean isActive, Integer duration, BigDecimal price) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isFalse(root.get("deleted")));

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("planName")), "%" + name.toLowerCase() + "%"));
            }
            if (isActive != null) {
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }
            if (duration != null) {
                predicates.add(cb.equal(root.get("duration"), duration));
            }
            if (price != null) {
                predicates.add(cb.equal(root.get("price"), price));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
