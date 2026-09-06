package com.zavrsniRad.LogisticsApp.domain.dto.transportUnits;

import com.zavrsniRad.LogisticsApp.domain.enums.Capability;
import com.zavrsniRad.LogisticsApp.domain.enums.TransportUnitType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransportUnitResponse {
    private UUID id;
    private TransportUnitType type;
    private String identifier;
    private Set<Capability> capabilities;
    private LocalDateTime createdAt;

}
