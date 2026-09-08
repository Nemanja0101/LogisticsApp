CREATE TABLE crew_members
(
    id             UUID PRIMARY KEY,
    user_id        UUID        NOT NULL UNIQUE REFERENCES users (id),
    license_number VARCHAR(50) NOT NULL,
    license_type   VARCHAR(50) NOT NULL,
    issued_at      DATE        NOT NULL,
    expires_at     DATE        NOT NULL,
    dangerous_goods_certified  BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at     TIMESTAMP   NOT NULL
);

CREATE TABLE crew_assignments
(
    id             UUID PRIMARY KEY,
    crew_member_id UUID      NOT NULL REFERENCES crew_members (id),
    shipment_segment_id    UUID      NOT NULL REFERENCES shipment_segments (id),
    created_at     TIMESTAMP NOT NULL
);