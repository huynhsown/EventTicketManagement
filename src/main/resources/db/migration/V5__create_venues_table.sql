CREATE TABLE venues
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    address     TEXT NOT NULL,
    city        VARCHAR(100) NOT NULL,
    country     VARCHAR(100) NOT NULL,
    latitude    DECIMAL(10, 7),
    longitude   DECIMAL(10, 7),
    capacity    INT NOT NULL,
    description TEXT,

    status      VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),
    deleted_at  TIMESTAMP,

    version     BIGINT  NOT NULL DEFAULT 0,

    CONSTRAINT chk_venue_capacity
        CHECK (capacity > 0),

    CONSTRAINT chk_venue_status
        CHECK (
            status IN (
                'ACTIVE',
                'INACTIVE',
                'UNDER_MAINTENANCE'
            )
        ),

    CONSTRAINT chk_venue_latitude
        CHECK (
            latitude IS NULL
            OR latitude BETWEEN -90 AND 90
        ),

    CONSTRAINT chk_venue_longitude
        CHECK (
            longitude IS NULL
            OR longitude BETWEEN -180 AND 180
        )
);

CREATE INDEX idx_venues_name ON venues(name);

CREATE INDEX idx_venues_city ON venues(city);

CREATE INDEX idx_venues_status ON venues(status);
