-- ===========================================
-- 🧾 INVOICE TABLOSU
-- ===========================================
CREATE TABLE IF NOT EXISTS invoices (
                                        id BIGSERIAL PRIMARY KEY,
                                        user_id BIGINT NOT NULL,
                                        invoice_number VARCHAR(100) NOT NULL UNIQUE,
    amount NUMERIC(15,2) NOT NULL,
    issue_date DATE,
    due_date DATE,
    company_name VARCHAR(255),
    address TEXT,
    phone VARCHAR(50),
    email VARCHAR(255),
    website VARCHAR(255),
    bank_account VARCHAR(100),

    CONSTRAINT fk_invoice_user
    FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE
    );

-- ===========================================
-- 📦 INVOICE ITEM TABLOSU
-- ===========================================
CREATE TABLE IF NOT EXISTS invoice_items (
                                             id BIGSERIAL PRIMARY KEY,
                                             description VARCHAR(255),
    unit_price NUMERIC(15,2),
    quantity INT,
    invoice_id BIGINT,

    CONSTRAINT fk_item_invoice
    FOREIGN KEY (invoice_id)
    REFERENCES invoices (id)
    ON DELETE CASCADE
    );
