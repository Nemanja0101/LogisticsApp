package com.zavrsniRad.LogisticsApp.domain.dto.shipment;

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
public class PatchShipmentOrder {

    private UUID startLocationId;

    private UUID endLocationId;
}
