ALTER TABLE subscription_plans
    ADD COLUMN stripe_product_id VARCHAR(255),
    ADD COLUMN stripe_price_id VARCHAR(255);