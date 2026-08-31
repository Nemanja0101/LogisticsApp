CREATE TABLE transport_units
(
    id             UUID PRIMARY KEY,
    created_at     TIMESTAMP   NOT NULL,
    transport_type VARCHAR(20) NOT NULL
);

CREATE TABLE trucks
(
    id              UUID PRIMARY KEY,
    trailer_type    VARCHAR(40)      NOT NULL,
    axle_count      INTEGER NOT NULL,
    manufacturer    VARCHAR(255) NOT NULL,
    model           VARCHAR(255) NOT NULL,
    license_plate   VARCHAR(255)     NOT NULL,
    max_weight_kg   INTEGER          NOT NULL,
    cargo_volume_m3 DOUBLE PRECISION NOT NULL,

    CONSTRAINT fk_trucks_transport_unit
        FOREIGN KEY (id)
            REFERENCES transport_units (id)
);

CREATE TABLE vans
(
    id              UUID PRIMARY KEY,
    manufacturer    VARCHAR(255) NOT NULL,
    model           VARCHAR(255) NOT NULL,
    license_plate   VARCHAR(255)     NOT NULL,
    max_weight_kg   INTEGER          NOT NULL,
    cargo_volume_m3 DOUBLE PRECISION NOT NULL,

    CONSTRAINT fk_vans_transport_unit
        FOREIGN KEY (id)
            REFERENCES transport_units (id)
);

CREATE TABLE vessels
(
    id               UUID PRIMARY KEY,
    imo_number       VARCHAR(255) NOT NULL UNIQUE,
    port_of_registry VARCHAR(255),
    operated_by      VARCHAR(255),

    CONSTRAINT fk_vessels_transport_unit
        FOREIGN KEY (id)
            REFERENCES transport_units (id)
);

CREATE TABLE railcars
(
    id           UUID PRIMARY KEY,
    wagon_number VARCHAR(255) NOT NULL UNIQUE,
    operated_by  VARCHAR(255) NOT NULL,

    CONSTRAINT fk_railcars_transport_unit
        FOREIGN KEY (id)
            REFERENCES transport_units (id)
);