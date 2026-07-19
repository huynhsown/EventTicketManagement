ALTER TABLE events
    ADD COLUMN slug VARCHAR(255) UNIQUE;

UPDATE events
SET slug = 'event-' || id
WHERE slug IS NULL;

ALTER TABLE events
    ALTER COLUMN slug SET NOT NULL;
