package com.genedu.subscription.model;

import com.genedu.commonlibrary.model.AbstractAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan extends AbstractAuditEntity{
    @Id
    @Column(name = "plan_id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "plan_name", nullable = false, length = Integer.MAX_VALUE)
    private String planName;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "duration")
    private Integer duration;

    @NotNull
    @Column(name = "stripe_product_id", nullable = false, unique = true, length = 255)
    private String stripeProductId;

    @NotNull
    @Column(name = "stripe_price_id", nullable = false, unique = true, length = 255)
    private String stripePriceId;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

}