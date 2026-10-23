ALTER TABLE customer.customer_addresses
    ALTER COLUMN country_code TYPE VARCHAR(2)
        USING TRIM(country_code)::VARCHAR(2);