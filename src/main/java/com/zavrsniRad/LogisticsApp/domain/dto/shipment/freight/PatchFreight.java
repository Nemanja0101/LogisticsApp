package com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight;


import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchFreight {

    private String name;

    private String description;

    @Positive
    private Double weightKg;

    @Positive
    private Double lengthCm;

    @Positive
    private Double widthCm;

    @Positive
    private Double heightCm;

}
