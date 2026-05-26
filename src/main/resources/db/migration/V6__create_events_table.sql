CREATE TABLE events
(
    id               BIGSERIAL PRIMARY KEY,
    organization_id  BIGINT NOT NULL REFERENCES organizations(id),
    venue_id         BIGINT NOT NULL REFERENCES venues(id),
    title            VARCHAR(255) NOT NULL,
    description      TEXT,
    banner_url       TEXT,
    category         VARCHAR(100),
    status           VARCHAR(30) NOT NULL,
    published_at     TIMESTAMP,

    created_at       TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP NOT NULL,
    created_by       VARCHAR(255),
    updated_by       VARCHAR(255),
    deleted_at       TIMESTAMP,

    version          BIGINT NOT NULL DEFAULT 0
);
