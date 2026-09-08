package com.zavrsniRad.LogisticsApp.domain.dto.transportUnits;

import com.zavrsniRad.LogisticsApp.domain.enums.Capability;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUpdateVessel {

    @NotBlank
    private String imoNumber;

    private String portOfRegistry;

    private String operatedBy;

    @NotNull
    @Positive
    private Double maxWeightKg;

    @NotNull
    @Positive
    private Double cargoVolumeM3;

    private Set<Capability> capabilities;

}
