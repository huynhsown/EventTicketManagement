CREATE TABLE sessions
(
    id              BIGSERIAL PRIMARY KEY,
    event_id        BIGINT NOT NULL REFERENCES events(id),
    start_time      TIMESTAMP NOT NULL,
    end_time        TIMESTAMP NOT NULL,
    sales_start_at  TIMESTAMP NOT NULL,
    sales_end_at    TIMESTAMP NOT NULL,
    status          VARCHAR(30) NOT NULL,

    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),
    deleted_at      TIMESTAMP,

    version         BIGINT NOT NULL DEFAULT 0
);
