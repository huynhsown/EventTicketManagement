CREATE INDEX idx_pending_reservations
    ON reservations(expires_at)
    WHERE status = 'PENDING';