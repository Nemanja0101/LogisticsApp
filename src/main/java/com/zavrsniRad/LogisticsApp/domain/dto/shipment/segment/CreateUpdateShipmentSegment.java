package com.zavrsniRad.LogisticsApp.domain.dto.shipment.segment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUpdateShipmentSegment {

    @NotNull
    @Positive
    private Short sequenceNumber;

    @NotNull
    private UUID transportUnitId;

    @NotNull
    private UUID startLocationId;

    @NotNull
    private UUID endLocationId;

}
