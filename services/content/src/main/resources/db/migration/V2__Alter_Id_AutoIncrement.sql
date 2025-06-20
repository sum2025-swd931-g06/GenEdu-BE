-- Tạo sequence cho các bảng
CREATE SEQUENCE IF NOT EXISTS subjects_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS materials_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS chapters_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS lessons_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS lesson_contents_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS lesson_content_media_files_id_seq START WITH 1 INCREMENT BY 1;

-- Alter table để id tự tăng theo sequence
ALTER TABLE subjects ALTER COLUMN id SET DEFAULT nextval('subjects_id_seq');
ALTER TABLE materials ALTER COLUMN id SET DEFAULT nextval('materials_id_seq');
ALTER TABLE chapters ALTER COLUMN id SET DEFAULT nextval('chapters_id_seq');
ALTER TABLE lessons ALTER COLUMN id SET DEFAULT nextval('lessons_id_seq');
ALTER TABLE lesson_contents ALTER COLUMN id SET DEFAULT nextval('lesson_contents_id_seq');
ALTER TABLE lesson_content_media_files ALTER COLUMN id SET DEFAULT nextval('lesson_content_media_files_id_seq');