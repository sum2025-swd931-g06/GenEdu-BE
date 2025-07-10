-- This script alters the columns and sets any existing data in them to NULL,
-- as a UUID cannot be converted to a Long/BIGINT.
-- This is safe for development but would require a data migration strategy in production.

ALTER TABLE finalized_lectures
    ALTER COLUMN audio_file_id        TYPE BIGINT USING (NULL),
    ALTER COLUMN presentation_file_id TYPE BIGINT USING (NULL),
    ALTER COLUMN video_file_id        TYPE BIGINT USING (NULL),
    ALTER COLUMN thumbnail_file_id    TYPE BIGINT USING (NULL);