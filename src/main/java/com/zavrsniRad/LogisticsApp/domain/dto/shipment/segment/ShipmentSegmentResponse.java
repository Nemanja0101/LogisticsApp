package com.zavrsniRad.LogisticsApp.domain.dto.shipment.segment;

import com.zavrsniRad.LogisticsApp.domain.dto.shipment.LocationResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.TransportUnitResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentSegmentResponse {

    private UUID id;
    private UUID shipmentId;
    private Short sequenceNumber;
    private TransportUnitResponse transportUnit;
    private LocationResponse startLocation;
    private LocationResponse endLocation;
    private LocalDateTime createdAt;


}
