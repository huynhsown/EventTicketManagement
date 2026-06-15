ALTER TABLE organizations
    ADD COLUMN owner_id BIGINT REFERENCES users (id);
