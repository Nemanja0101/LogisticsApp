package com.zavrsniRad.LogisticsApp.domain.dto.shipment;

import com.zavrsniRad.LogisticsApp.domain.enums.ShipmentOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateShipmentOrderStatus {

    private ShipmentOrderStatus status;
}