ALTER TABLE slide_contents
    ADD COLUMN narration_file_id BIGINT;
ALTER TABLE slide_contents
    DROP COLUMN main_idea;
ALTER TABLE slide_contents
    ADD COLUMN slide_type VARCHAR(100) DEFAULT 'CONTENT' NOT NULL;