CREATE TABLE IF NOT EXISTS mails (
                                     id BIGSERIAL PRIMARY KEY,
                                     sender VARCHAR(255) NOT NULL,
    recipient VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    sent_date TIMESTAMP,
    status VARCHAR(100)
    );
