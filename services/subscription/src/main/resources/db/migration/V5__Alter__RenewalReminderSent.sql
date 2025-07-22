ALTER TABLE subscriptions
    DROP COLUMN renewal_reminder_sent;
ALTER TABLE subscriptions
    ADD COLUMN renewal_reminder_sent BOOLEAN NOT NULL DEFAULT FALSE;