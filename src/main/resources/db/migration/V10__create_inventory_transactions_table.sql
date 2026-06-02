CREATE TABLE inventory_transactions
(
    id                  BIGSERIAL PRIMARY KEY,

    ticket_type_id      BIGINT NOT NULL REFERENCES ticket_types(id),

    type                VARCHAR(30) NOT NULL,

    quantity            INTEGER NOT NULL,

    reference_id        UUID,

    note                TEXT,

    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP NOT NULL,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    deleted_at          TIMESTAMP,

    version             BIGINT NOT NULL DEFAULT 0
);
