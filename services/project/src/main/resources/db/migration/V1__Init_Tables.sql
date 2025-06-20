CREATE TABLE finalized_lectures
(
    id                   UUID                          NOT NULL,
    created_on           TIMESTAMP WITHOUT TIME ZONE,
    last_modified_on     TIMESTAMP WITHOUT TIME ZONE,
    is_deleted           BOOLEAN                       NOT NULL,
    lecture_content_id   UUID                          NOT NULL,
    audio_file_id        UUID,
    presentation_file_id UUID,
    video_file_id        UUID,
    thumbnail_file_id    UUID,
    published_status     VARCHAR(20) DEFAULT 'PRIVATE' NOT NULL,
    CONSTRAINT pk_finalized_lectures PRIMARY KEY (id)
);

CREATE TABLE lecture_contents
(
    id               UUID                        NOT NULL,
    created_on       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_on TIMESTAMP WITHOUT TIME ZONE,
    is_deleted       BOOLEAN                     NOT NULL,
    project_id       UUID                        NOT NULL,
    version          VARCHAR(20) DEFAULT 'v1',
    title            VARCHAR(150)                NOT NULL,
    status           VARCHAR(20) DEFAULT 'DRAFT' NOT NULL,
    CONSTRAINT pk_lecture_contents PRIMARY KEY (id)
);

CREATE TABLE projects
(
    id                  UUID                        NOT NULL,
    created_on          TIMESTAMP WITHOUT TIME ZONE,
    last_modified_on    TIMESTAMP WITHOUT TIME ZONE,
    is_deleted          BOOLEAN                     NOT NULL,
    user_id             UUID                        NOT NULL,
    lesson_id           BIGINT                      NOT NULL,
    title               VARCHAR(255)                NOT NULL,
    lesson_plan_file_id BIGINT,
    custom_instructions TEXT,
    status              VARCHAR(20) DEFAULT 'DRAFT' NOT NULL,
    slide_num           INTEGER     DEFAULT 10,
    template_id         BIGINT,
    CONSTRAINT pk_projects PRIMARY KEY (id)
);

CREATE TABLE slide_contents
(
    id                 UUID    NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    last_modified_on   TIMESTAMP WITHOUT TIME ZONE,
    is_deleted         BOOLEAN NOT NULL,
    lecture_content_id UUID    NOT NULL,
    order_number       INTEGER NOT NULL,
    slide_title        VARCHAR(150),
    main_idea          TEXT    NOT NULL,
    subpoints          JSONB   NOT NULL,
    narration_script   TEXT,
    CONSTRAINT pk_slide_contents PRIMARY KEY (id)
);

ALTER TABLE finalized_lectures
    ADD CONSTRAINT uc_finalized_lectures_lecture_content UNIQUE (lecture_content_id);

ALTER TABLE finalized_lectures
    ADD CONSTRAINT FK_FINALIZED_LECTURES_ON_LECTURE_CONTENT FOREIGN KEY (lecture_content_id) REFERENCES lecture_contents (id);

ALTER TABLE lecture_contents
    ADD CONSTRAINT FK_LECTURE_CONTENTS_ON_PROJECT FOREIGN KEY (project_id) REFERENCES projects (id);

ALTER TABLE slide_contents
    ADD CONSTRAINT FK_SLIDE_CONTENTS_ON_LECTURE_CONTENT FOREIGN KEY (lecture_content_id) REFERENCES lecture_contents (id);