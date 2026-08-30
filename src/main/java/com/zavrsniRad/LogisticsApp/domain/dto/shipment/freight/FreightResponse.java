package com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight;


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
public class FreightResponse {

    private UUID id;
    private String name;
    private String description;
    private Double weightKg;
    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;

    private UUID shipmentOrderId;
    private UUID shipmentId;

    private LocalDateTime createdAt;

}
