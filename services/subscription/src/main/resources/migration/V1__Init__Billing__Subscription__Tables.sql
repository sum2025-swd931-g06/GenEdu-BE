-- Bảng user_billing_accounts
CREATE TABLE user_billing_accounts
(
    account_id                  UUID PRIMARY KEY,
    user_id                     UUID    NOT NULL,
    payment_gateway_customer_id VARCHAR NOT NULL UNIQUE,
    subscription_status     BOOLEAN NOT NULL DEFAULT FALSE
);

-- Bảng subscription_plans
CREATE TABLE subscription_plans
(
    plan_id     UUID PRIMARY KEY,
    plan_name   VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    price       DECIMAL NOT NULL,
    duration    INT,
    is_deleted  BOOLEAN NOT NULL DEFAULT FALSE
);

-- Bảng subscriptions
CREATE TABLE subscriptions
(
    subscription_id UUID PRIMARY KEY,
    account_id      UUID      NOT NULL,
    plan_id         UUID      NOT NULL,
    started_at      TIMESTAMP NOT NULL,
    ended_at        TIMESTAMP NOT NULL,
    auto_renew      BOOLEAN   NOT NULL,
    status          VARCHAR   NOT NULL,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    CONSTRAINT fk_subscriptions_account FOREIGN KEY (account_id) REFERENCES user_billing_accounts (account_id),
    CONSTRAINT fk_subscriptions_plan FOREIGN KEY (plan_id) REFERENCES subscription_plans (plan_id)
);

-- Bảng user_transactions
CREATE TABLE user_transactions
(
    transaction_id UUID PRIMARY KEY,
    account_id     UUID      NOT NULL,
    amount         DECIMAL   NOT NULL,
    created_at     TIMESTAMP NOT NULL,
    status         VARCHAR   NOT NULL,
    CONSTRAINT fk_transactions_account FOREIGN KEY (account_id) REFERENCES user_billing_accounts (account_id)
);