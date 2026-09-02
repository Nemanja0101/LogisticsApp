package com.zavrsniRad.LogisticsApp.mappers;

import com.zavrsniRad.LogisticsApp.domain.dto.crew.CrewAssignmentResponse;
import com.zavrsniRad.LogisticsApp.domain.models.crew.CrewAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = CrewMemberMapper.class)
public interface CrewAssignmentMapper {

    @Mapping(target = "shipmentSegmentId", source = "shipmentSegment.id")
    CrewAssignmentResponse toResponse(CrewAssignment crewAssignment);

}
