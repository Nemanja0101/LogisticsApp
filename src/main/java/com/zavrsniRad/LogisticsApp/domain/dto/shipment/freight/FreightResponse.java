package com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight;


import com.zavrsniRad.LogisticsApp.domain.enums.Capability;
import com.zavrsniRad.LogisticsApp.domain.enums.FreightForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreightResponse {

    private UUID id;
    private FreightForm form;
    private Set<Capability> requirements;
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
