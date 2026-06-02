CREATE TABLE reservations
(
    id                  UUID PRIMARY KEY,
    user_id             UUID NOT NULL REFERENCES users(id),
    ticket_type_id      BIGINT NOT NULL REFERENCES ticket_types(id),
    quantity            INTEGER NOT NULL,
    status              VARCHAR(30) NOT NULL,
    expires_at          TIMESTAMP NOT NULL,

    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP NOT NULL,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    deleted_at          TIMESTAMP,

    version             BIGINT NOT NULL DEFAULT 0
);
