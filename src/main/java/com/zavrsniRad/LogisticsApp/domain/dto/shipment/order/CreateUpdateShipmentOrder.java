package com.zavrsniRad.LogisticsApp.domain.dto.shipment.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUpdateShipmentOrder {

    @NotNull
    private UUID startLocationId;

    @NotNull
    private UUID endLocationId;
}