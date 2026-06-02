CREATE TABLE payments
(
    id                  UUID PRIMARY KEY,
    order_id            UUID NOT NULL REFERENCES orders(id),
    provider            VARCHAR(50),
    transaction_id      VARCHAR(255),
    amount              NUMERIC(12,2) NOT NULL,
    status              VARCHAR(30) NOT NULL,
    paid_at             TIMESTAMP,

    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP NOT NULL,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    deleted_at          TIMESTAMP,

    version             BIGINT NOT NULL DEFAULT 0
);
