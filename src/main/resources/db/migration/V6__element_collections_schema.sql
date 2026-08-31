CREATE TABLE freight_requirements
(
    freight_id UUID        NOT NULL REFERENCES freights (id) ON DELETE CASCADE,
    capability VARCHAR(30) NOT NULL,
    PRIMARY KEY (freight_id, capability)
);

CREATE TABLE transport_unit_capabilities
(
    transport_unit_id UUID        NOT NULL REFERENCES transport_units (id) ON DELETE CASCADE,
    capability        VARCHAR(30) NOT NULL,
    PRIMARY KEY (transport_unit_id, capability)
);