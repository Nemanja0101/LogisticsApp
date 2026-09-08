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
public class VesselResponse {

    private UUID id;
    private LocalDateTime createdAt;
    private Set<Capability> capabilities;
    private Double maxWeightKg;
    private Double cargoVolumeM3;
    private String imoNumber;
    private String portOfRegistry;
    private String operatedBy;

}
