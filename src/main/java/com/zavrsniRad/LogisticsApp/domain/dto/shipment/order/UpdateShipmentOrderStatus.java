package com.zavrsniRad.LogisticsApp.domain.dto.shipment.order;

import com.zavrsniRad.LogisticsApp.domain.enums.ShipmentOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateShipmentOrderStatus {

    @NotNull
    private ShipmentOrderStatus status;
}