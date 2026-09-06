package com.zavrsniRad.LogisticsApp.domain.dto.crew;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrewAssignmentResponse {

    private UUID id;
    private CrewMemberResponse crewMember;
    private UUID shipmentSegmentId;
    private LocalDateTime createdAt;

}
