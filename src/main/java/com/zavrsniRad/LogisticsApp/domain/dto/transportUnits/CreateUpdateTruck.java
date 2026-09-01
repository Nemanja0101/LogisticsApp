package com.zavrsniRad.LogisticsApp.domain.dto.transportUnits;

import com.zavrsniRad.LogisticsApp.domain.enums.Capability;
import com.zavrsniRad.LogisticsApp.domain.enums.TrailerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUpdateTruck {

    @NotNull
    private TrailerType trailerType;

    @NotNull
    @Positive
    private Integer axleCount;

    @NotBlank
    private String licensePlate;

    @NotNull
    @Positive
    private Integer maxWeightKg;

    @NotNull
    @Positive
    private Double cargoVolumeM3;

    @NotBlank
    private String manufacturer;

    @NotBlank
    private String model;

    private Set<Capability> capabilities;

}
