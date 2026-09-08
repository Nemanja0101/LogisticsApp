package com.zavrsniRad.LogisticsApp.domain.dto.transportUnits;

import com.zavrsniRad.LogisticsApp.domain.enums.Capability;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AircraftResponse {

    private UUID id;
    private LocalDateTime createdAt;
    private Set<Capability> capabilities;
    private String registrationNumber;
    private String manufacturer;
    private String model;
    private Double maxWeightKg;
    private Double cargoVolumeM3;
    private Integer maxRangeKm;
    private String operatedBy;

}
