CREATE TABLE IF NOT EXISTS calendars (
                                         id BIGSERIAL PRIMARY KEY,

                                         title VARCHAR(255) NOT NULL,
    notes TEXT,
    event_date TIMESTAMP,

    parent_calendar_id BIGINT,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_calendar_parent
    FOREIGN KEY (parent_calendar_id)
    REFERENCES calendars (id)
    ON DELETE SET NULL,

    CONSTRAINT fk_calendar_user
    FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE
    );
