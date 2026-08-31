CREATE TABLE shipment_segments
(
    id                UUID PRIMARY KEY,
    shipment_id       UUID NOT NULL references shipments (id) ON DELETE CASCADE,
    transport_unit_id UUID NOT NULL references transport_units(id),
    sequence_number   SMALLINT NOT NULL,
    start_location_id UUID NOT NULL references locations (id),
    end_location_id   UUID NOT NULL references locations (id),
    created_at        TIMESTAMP    NOT NULL DEFAULT now()
);
