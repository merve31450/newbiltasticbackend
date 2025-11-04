ALTER TABLE invoices
    ADD COLUMN user_id BIGINT;

-- 2. Mevcut kayıtlar için varsayılan kullanıcı ata (örnek: id = 1)
UPDATE invoices SET user_id = 1 WHERE user_id IS NULL;

-- 3. Şimdi NOT NULL kısıtlamasını uygula
ALTER TABLE invoices
    ALTER COLUMN user_id SET NOT NULL;

-- 4. Yabancı anahtar (foreign key) ekle
ALTER TABLE invoices
    ADD CONSTRAINT fk_invoice_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;