package com.zavrsniRad.LogisticsApp.domain.dto.shipment;

import com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight.FreightResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponse {

    private UUID id;
    private List<FreightResponse> freights;
    private LocalDateTime createdAt;




}
