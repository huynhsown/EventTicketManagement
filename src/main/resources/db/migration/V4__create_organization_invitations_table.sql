CREATE TABLE organization_invitations
(
    id              UUID PRIMARY KEY,
    organization_id BIGINT              NOT NULL REFERENCES organizations (id),
    email           VARCHAR(255)        NOT NULL,
    role            VARCHAR(30)         NOT NULL,
    invited_by      UUID                NOT NULL REFERENCES users (id),
    token           VARCHAR(255) UNIQUE NOT NULL,
    status          VARCHAR(30)         NOT NULL,
    expires_at      TIMESTAMP           NOT NULL,

    created_at      TIMESTAMP           NOT NULL,
    updated_at      TIMESTAMP           NOT NULL,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),
    deleted_at      TIMESTAMP,

    version         BIGINT              NOT NULL DEFAULT 0
);
