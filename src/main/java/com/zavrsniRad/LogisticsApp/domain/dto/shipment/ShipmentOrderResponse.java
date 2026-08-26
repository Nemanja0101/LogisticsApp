package com.zavrsniRad.LogisticsApp.domain.dto.shipment;

import com.zavrsniRad.LogisticsApp.domain.enums.ShipmentOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentOrderResponse {

    private UUID id;
    private UUID customerId;
    private LocationResponse startLocation;
    private LocationResponse endLocation;
    private int freightCount;
    private ShipmentOrderStatus status;
    private LocalDateTime createdAt;



}
