CREATE TABLE categories
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    slug          VARCHAR(100) NOT NULL UNIQUE,
    description   TEXT,
    display_order INTEGER,
    status        VARCHAR(30) NOT NULL,

    created_at    TIMESTAMP NOT NULL,
    updated_at    TIMESTAMP NOT NULL,
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    deleted_at    TIMESTAMP,

    version       BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE event_categories
(
    event_id    BIGINT NOT NULL REFERENCES events(id),
    category_id BIGINT NOT NULL REFERENCES categories(id),

    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),
    deleted_at  TIMESTAMP,

    version     BIGINT NOT NULL DEFAULT 0,

    PRIMARY KEY (event_id, category_id)
);
