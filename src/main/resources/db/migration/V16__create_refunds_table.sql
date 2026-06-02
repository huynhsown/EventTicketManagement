CREATE TABLE refunds
(
    id                  UUID PRIMARY KEY,

    payment_id          UUID NOT NULL REFERENCES payments(id),

    amount              NUMERIC(12,2) NOT NULL,

    reason              TEXT,

    status              VARCHAR(30) NOT NULL,

    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP NOT NULL,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    deleted_at          TIMESTAMP,

    version             BIGINT NOT NULL DEFAULT 0
);
