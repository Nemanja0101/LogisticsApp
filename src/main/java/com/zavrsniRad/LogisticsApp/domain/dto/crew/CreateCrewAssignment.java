package com.zavrsniRad.LogisticsApp.domain.dto.crew;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCrewAssignment {

    @NotNull
    private UUID crewMemberId;

    @NotNull
    private UUID shipmentSegmentId;

}
