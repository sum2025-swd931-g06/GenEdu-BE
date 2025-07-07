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

//    @Column(name = "created_on")
//    private Instant createdOn;
//
//    @Column(name = "last_modified_on")
//    private Instant lastModifiedOn;
//
//    @Column(name = "created_by")
//    private UUID createdBy;
//
//    @Column(name = "last_modified_by")
//    private UUID lastModifiedBy;
//
//    @NotNull
//    @ColumnDefault("false")
//    @Column(name = "is_deleted", nullable = false)
//    private Boolean isDeleted = false;

}