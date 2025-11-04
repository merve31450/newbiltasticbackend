CREATE TABLE IF NOT EXISTS cards (
                                     id BIGSERIAL PRIMARY KEY,
                                     card_number VARCHAR(20) NOT NULL,
    card_holder_name VARCHAR(100) NOT NULL,
    expiration_date VARCHAR(5) NOT NULL,
    cvc VARCHAR(4) NOT NULL
    );
