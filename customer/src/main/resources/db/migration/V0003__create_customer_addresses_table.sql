CREATE TABLE IF NOT EXISTS customer.customer_addresses
(
    id           UUID PRIMARY KEY,
    customer_id  UUID         NOT NULL,
    address_type VARCHAR(50)  NOT NULL,
    line1        VARCHAR(255) NOT NULL,
    line2        VARCHAR(255),
    city         VARCHAR(100) NOT NULL,
    pincode      VARCHAR(20)  NOT NULL,
    country_code CHAR(2)      NOT NULL,
    is_current   BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_addresses_customer_id ON customer.customer_addresses (customer_id);