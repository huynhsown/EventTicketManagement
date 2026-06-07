CREATE TABLE organization_members (
    organization_id BIGINT NOT NULL REFERENCES organizations(id),
    user_id BIGINT NOT NULL REFERENCES users(id),

    role VARCHAR(30) NOT NULL,

    status VARCHAR(30) NOT NULL,

    joined_at TIMESTAMP NOT NULL,

    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    deleted_at TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,

    PRIMARY KEY (organization_id, user_id)
);
