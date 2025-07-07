ALTER TABLE notifications RENAME COLUMN user_id TO email;

ALTER TABLE user_device_tokens RENAME COLUMN user_id TO email;