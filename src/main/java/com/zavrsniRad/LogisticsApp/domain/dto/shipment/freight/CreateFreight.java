package com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight;


import com.zavrsniRad.LogisticsApp.domain.enums.Capability;
import com.zavrsniRad.LogisticsApp.domain.enums.FreightForm;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFreight {

    @NotEmpty
    private String name;

    @NotNull
    private FreightForm form;

    private Set<Capability> requirements;

    private String description;

    @NotNull
    @Positive
    private Double weightKg;

    @NotNull
    @Positive
    private Double lengthCm;

    @NotNull
    @Positive
    private Double widthCm;

    @NotNull
    @Positive
    private Double heightCm;



}
