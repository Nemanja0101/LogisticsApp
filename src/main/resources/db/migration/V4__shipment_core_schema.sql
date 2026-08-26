CREATE TABLE locations
(
    id        UUID PRIMARY KEY,
    name      VARCHAR(60),
    address   VARCHAR(80)  NOT NULL,
    city      VARCHAR(100) NOT NULL,
    country   VARCHAR(60)  NOT NULL,
    latitude  DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);

CREATE TABLE shipment_orders
(
    id          UUID PRIMARY KEY,
    status      VARCHAR(30) NOT NULL,
    customer_id UUID        NOT NULL references users (id),
    created_at  TIMESTAMP   NOT NULL DEFAULT now(),
    start_location_id UUID NOT NULL references locations (id),
    end_location_id   UUID NOT NULL references locations (id)
);

CREATE TABLE shipments
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE freights
(
    id                UUID PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    description       TEXT,
    weight_kg         DOUBLE PRECISION NOT NULL,
    length_cm         DOUBLE PRECISION NOT NULL,
    width_cm          DOUBLE PRECISION NOT NULL,
    height_cm         DOUBLE PRECISION NOT NULL,
    shipment_order_id UUID         NOT NULL references shipment_orders (id),
    shipment_id       UUID references shipments (id),
    created_at        TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE shipment_segments
(
    id                UUID PRIMARY KEY,
    shipment_id       UUID NOT NULL references shipments (id),
    sequence_number   SMALLINT NOT NULL,
    start_location_id UUID NOT NULL references locations (id),
    end_location_id   UUID NOT NULL references locations (id),
    created_at        TIMESTAMP    NOT NULL DEFAULT now()
);

