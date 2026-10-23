ALTER TABLE customer.customers
    ALTER COLUMN nationality TYPE VARCHAR(2)
        USING TRIM(nationality)::VARCHAR(2);