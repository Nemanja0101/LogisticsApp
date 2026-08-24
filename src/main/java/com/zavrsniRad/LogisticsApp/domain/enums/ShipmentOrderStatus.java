package com.zavrsniRad.LogisticsApp.domain.enums;

public enum ShipmentOrderStatus {
    PENDING,           // order placed, not yet reviewed/accepted
    CONFIRMED,         // accepted, freight defined, awaiting assignment to a shipment
    ASSIGNED,          // freight loaded onto one or more shipments, awaiting departure
    IN_TRANSIT,        // at least one shipment carrying its freight has departed
    PARTIALLY_DELIVERED, // some but not all freight delivered (relevant once you support splitting across shipments)
    DELIVERED,         // all freight for this order delivered
    CANCELLED,
    ON_HOLD            // exception state — payment issue, customer requested pause, etc.
}
