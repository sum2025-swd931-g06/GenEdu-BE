CREATE TABLE subscription_plans
(
    plan_id          UUID    NOT NULL,
    created_on       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_on TIMESTAMP WITHOUT TIME ZONE,
    created_by       UUID,
    last_modified_by UUID,
    is_deleted       BOOLEAN NOT NULL,
    plan_name        TEXT    NOT NULL,
    description      TEXT    NOT NULL,
    price            DECIMAL NOT NULL,
    duration         INTEGER,
    CONSTRAINT pk_subscription_plans PRIMARY KEY (plan_id)
);

CREATE TABLE subscriptions
(
    subscription_id UUID                        NOT NULL,
    account_id      UUID                        NOT NULL,
    plan_id         UUID                        NOT NULL,
    started_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ended_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    auto_renew      BOOLEAN                     NOT NULL,
    status          TEXT                        NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_subscriptions PRIMARY KEY (subscription_id)
);

CREATE TABLE user_billing_accounts
(
    account_id                  UUID                  NOT NULL,
    user_id                     UUID                  NOT NULL,
    payment_gateway_customer_id TEXT                  NOT NULL,
    subscription_status         BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT pk_user_billing_accounts PRIMARY KEY (account_id)
);

CREATE TABLE user_transactions
(
    transaction_id UUID                        NOT NULL,
    account_id     UUID                        NOT NULL,
    amount         DECIMAL                     NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status         TEXT                        NOT NULL,
    CONSTRAINT pk_user_transactions PRIMARY KEY (transaction_id)
);

ALTER TABLE user_billing_accounts
    ADD CONSTRAINT user_billing_accounts_payment_gateway_customer_id_key UNIQUE (payment_gateway_customer_id);

ALTER TABLE subscriptions
    ADD CONSTRAINT FK_SUBSCRIPTIONS_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES user_billing_accounts (account_id);

ALTER TABLE subscriptions
    ADD CONSTRAINT FK_SUBSCRIPTIONS_ON_PLAN FOREIGN KEY (plan_id) REFERENCES subscription_plans (plan_id);

ALTER TABLE user_transactions
    ADD CONSTRAINT FK_USER_TRANSACTIONS_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES user_billing_accounts (account_id);