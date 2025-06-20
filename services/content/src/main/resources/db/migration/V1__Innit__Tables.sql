CREATE SEQUENCE IF NOT EXISTS chapters_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE chapters
(
    id               BIGINT       NOT NULL,
    created_on       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_on TIMESTAMP WITHOUT TIME ZONE,
    created_by       UUID,
    last_modified_by UUID,
    is_deleted       BOOLEAN      NOT NULL,
    material_id      BIGINT,
    title            VARCHAR(150) NOT NULL,
    order_number     INTEGER      NOT NULL,
    description      TEXT,
    CONSTRAINT pk_chapters PRIMARY KEY (id)
);

CREATE TABLE lesson_content_media_files
(
    id                BIGINT            NOT NULL,
    lesson_content_id BIGINT            NOT NULL,
    media_file_id     BIGINT            NOT NULL,
    order_number      INTEGER DEFAULT 0 NOT NULL,
    description       TEXT,
    CONSTRAINT pk_lesson_content_media_files PRIMARY KEY (id)
);

CREATE TABLE lesson_contents
(
    id               BIGINT       NOT NULL,
    created_on       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_on TIMESTAMP WITHOUT TIME ZONE,
    created_by       UUID,
    last_modified_by UUID,
    is_deleted       BOOLEAN      NOT NULL,
    lesson_id        BIGINT,
    title            VARCHAR(150) NOT NULL,
    order_number     INTEGER      NOT NULL,
    content          TEXT         NOT NULL,
    CONSTRAINT pk_lesson_contents PRIMARY KEY (id)
);

CREATE TABLE lessons
(
    id               BIGINT       NOT NULL,
    created_on       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_on TIMESTAMP WITHOUT TIME ZONE,
    created_by       UUID,
    last_modified_by UUID,
    is_deleted       BOOLEAN      NOT NULL,
    chapter_id       BIGINT,
    title            VARCHAR(150) NOT NULL,
    order_number     INTEGER      NOT NULL,
    description      VARCHAR(255),
    status           VARCHAR(20) DEFAULT 'UN_SYNC',
    CONSTRAINT pk_lessons PRIMARY KEY (id)
);

CREATE TABLE materials
(
    id               BIGINT       NOT NULL,
    created_on       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_on TIMESTAMP WITHOUT TIME ZONE,
    created_by       UUID,
    last_modified_by UUID,
    is_deleted       BOOLEAN      NOT NULL,
    title            VARCHAR(150) NOT NULL,
    order_number     INTEGER      NOT NULL,
    description      TEXT,
    subject_id       INTEGER,
    CONSTRAINT pk_materials PRIMARY KEY (id)
);

CREATE TABLE school_classes
(
    id               INTEGER      NOT NULL,
    created_on       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_on TIMESTAMP WITHOUT TIME ZONE,
    created_by       UUID,
    last_modified_by UUID,
    is_deleted       BOOLEAN      NOT NULL,
    name             VARCHAR(100) NOT NULL,
    description      TEXT,
    CONSTRAINT pk_school_classes PRIMARY KEY (id)
);

CREATE TABLE subjects
(
    id               INTEGER      NOT NULL,
    created_on       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_on TIMESTAMP WITHOUT TIME ZONE,
    created_by       UUID,
    last_modified_by UUID,
    is_deleted       BOOLEAN      NOT NULL,
    name             VARCHAR(100) NOT NULL,
    description      TEXT,
    school_class_id  INTEGER,
    CONSTRAINT pk_subjects PRIMARY KEY (id)
);

ALTER TABLE chapters
    ADD CONSTRAINT FK_CHAPTERS_ON_MATERIAL FOREIGN KEY (material_id) REFERENCES materials (id);

ALTER TABLE lessons
    ADD CONSTRAINT FK_LESSONS_ON_CHAPTER FOREIGN KEY (chapter_id) REFERENCES chapters (id);

ALTER TABLE lesson_contents
    ADD CONSTRAINT FK_LESSON_CONTENTS_ON_LESSON FOREIGN KEY (lesson_id) REFERENCES lessons (id);

ALTER TABLE lesson_content_media_files
    ADD CONSTRAINT FK_LESSON_CONTENT_MEDIA_FILES_ON_LESSON_CONTENT FOREIGN KEY (lesson_content_id) REFERENCES lesson_contents (id);

ALTER TABLE materials
    ADD CONSTRAINT FK_MATERIALS_ON_SUBJECT FOREIGN KEY (subject_id) REFERENCES subjects (id);

ALTER TABLE subjects
    ADD CONSTRAINT FK_SUBJECTS_ON_SCHOOL_CLASS FOREIGN KEY (school_class_id) REFERENCES school_classes (id);