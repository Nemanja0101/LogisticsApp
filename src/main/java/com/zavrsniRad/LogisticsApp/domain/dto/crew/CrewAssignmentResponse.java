package com.zavrsniRad.LogisticsApp.domain.dto.crew;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
