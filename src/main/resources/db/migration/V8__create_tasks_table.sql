CREATE TABLE IF NOT EXISTS tasks (
                                     id BIGSERIAL PRIMARY KEY,
                                     task VARCHAR(255),
    collection VARCHAR(255),
    description TEXT,
    badge VARCHAR(50),
    remind_at TIMESTAMP,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    category_id BIGINT,

    CONSTRAINT fk_task_category
    FOREIGN KEY (category_id)
    REFERENCES task_categories (id)
    ON DELETE SET NULL
    );
