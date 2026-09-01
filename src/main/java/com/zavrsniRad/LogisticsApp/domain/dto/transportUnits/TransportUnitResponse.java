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
public class TransportUnitResponse {
//TODO:: should it only expose these?
    private UUID id;
    private Set<Capability> capabilities;
    private LocalDateTime createdAt;

}
