CREATE TABLE ticket_types
(
    id              BIGSERIAL PRIMARY KEY,
    session_id      BIGINT NOT NULL REFERENCES sessions(id),
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
    price           DECIMAL(12,2) NOT NULL,
    max_per_user    INTEGER NOT NULL,

    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),
    deleted_at      TIMESTAMP,

    version         BIGINT NOT NULL DEFAULT 0
);
