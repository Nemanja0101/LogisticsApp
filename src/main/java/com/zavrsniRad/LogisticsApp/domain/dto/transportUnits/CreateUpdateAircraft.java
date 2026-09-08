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
public class CreateUpdateAircraft {

    @NotBlank
    private String registrationNumber;

    @NotBlank
    private String manufacturer;

    @NotBlank
    private String model;

    @NotNull
    @Positive
    private Double maxWeightKg;

    @NotNull
    @Positive
    private Double cargoVolumeM3;

    @NotNull
    @Positive
    private Integer maxRangeKm;

    private String operatedBy;

    private Set<Capability> capabilities;

}
