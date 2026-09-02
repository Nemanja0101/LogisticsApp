package com.zavrsniRad.LogisticsApp.domain.dto.shipment.segment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
