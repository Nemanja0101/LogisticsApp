package com.zavrsniRad.LogisticsApp.domain.dto.transportUnits;
import com.zavrsniRad.LogisticsApp.domain.enums.Capability;
import com.zavrsniRad.LogisticsApp.domain.enums.TrailerType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TruckResponse {

    private UUID id;
    private LocalDateTime createdAt;
    private Set<Capability> capabilities;
    private TrailerType trailerType;
    private Integer axleCount;
    private String licensePlate;
    private Double maxWeightKg;
    private Double cargoVolumeM3;
    private String manufacturer;
    private String model;

}
