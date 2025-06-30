package com.genedu.subscription.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_billing_accounts", uniqueConstraints = {
        @UniqueConstraint(name = "user_billing_accounts_payment_gateway_customer_id_key", columnNames = {"payment_gateway_customer_id"})
})
public class UserBillingAccount {
    @Id
    @Column(name = "account_id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotNull
    @Column(name = "payment_gateway_customer_id", nullable = false, length = Integer.MAX_VALUE)
    private String paymentGatewayCustomerId;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "subscription_status", nullable = false)
    private Boolean subscriptionStatus = false;

}