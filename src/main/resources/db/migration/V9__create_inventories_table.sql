CREATE TABLE inventories
(
    ticket_type_id      BIGINT PRIMARY KEY REFERENCES ticket_types(id),

    total_stock         INTEGER NOT NULL,

    reserved_stock      INTEGER NOT NULL DEFAULT 0,

    sold_stock          INTEGER NOT NULL DEFAULT 0,

    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP NOT NULL,
    created_by          VARCHAR(255),
    updated_by          VARCHAR(255),
    deleted_at          TIMESTAMP,

    version             BIGINT NOT NULL DEFAULT 0
);
