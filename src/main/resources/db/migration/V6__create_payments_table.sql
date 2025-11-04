CREATE TABLE IF NOT EXISTS payments (
                                        id BIGSERIAL PRIMARY KEY,
                                        amount NUMERIC(15,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    status VARCHAR(50) NOT NULL,
    payment_date TIMESTAMP,
    transaction_id VARCHAR(100),
    card_number VARCHAR(20),
    card_holder_name VARCHAR(100),
    expiry_date VARCHAR(10),
    cvc VARCHAR(4),
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_payment_user
    FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE
    );
