CREATE TABLE tickets
(
    id                  UUID PRIMARY KEY,
    order_item_id       BIGINT NOT NULL REFERENCES order_items(id),
    ticket_code         VARCHAR(100) UNIQUE NOT NULL,
    qr_code             TEXT NOT NULL,
    attendee_name       VARCHAR(255),
    attendee_email      VARCHAR(255),
    status              VARCHAR(30) NOT NULL,
    issued_at           TIMESTAMP,

    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP NOT NULL,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    deleted_at          TIMESTAMP,

    version             BIGINT NOT NULL DEFAULT 0
);
