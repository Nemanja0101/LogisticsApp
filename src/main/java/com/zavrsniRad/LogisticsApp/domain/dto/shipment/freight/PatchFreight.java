package com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight;


import com.zavrsniRad.LogisticsApp.domain.enums.Capability;
import com.zavrsniRad.LogisticsApp.domain.enums.FreightForm;
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
public class PatchFreight {

    private String name;

    private FreightForm form;

    private Set<Capability> requirements;

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
