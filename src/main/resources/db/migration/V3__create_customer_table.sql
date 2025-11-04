CREATE TABLE IF NOT EXISTS customers (
                                         id BIGSERIAL PRIMARY KEY,
                                         company_name VARCHAR(255) NOT NULL,
    contact_name VARCHAR(255),
    invoice_email VARCHAR(255),
    invoice_number VARCHAR(100),
    euro_amount NUMERIC(15,2),
    dollar_amount NUMERIC(15,2),
    tl_amount NUMERIC(15,2),
    priority VARCHAR(50),
    receivable_total NUMERIC(15,2),
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_customer_user
    FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE
    );
