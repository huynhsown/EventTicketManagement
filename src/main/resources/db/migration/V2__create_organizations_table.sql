CREATE TABLE organizations
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)        NOT NULL,
    slug        VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    logo_url    TEXT,
    website     VARCHAR(255),
    status      VARCHAR(30)         NOT NULL,

    created_by  UUID                NOT NULL REFERENCES users (id),
    updated_by  VARCHAR(255),
    created_at  TIMESTAMP           NOT NULL,
    updated_at  TIMESTAMP           NOT NULL,
    deleted_at  TIMESTAMP,

    version     BIGINT              NOT NULL DEFAULT 0
);
