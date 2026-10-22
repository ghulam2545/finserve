CREATE TABLE IF NOT EXISTS auth.users
(
    id         UUID PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(72)  NOT NULL,
    role       VARCHAR(50)  NOT NULL,
    enabled    BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL,
    version    BIGINT       NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_users_email ON auth.users (email);
