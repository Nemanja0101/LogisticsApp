package com.zavrsniRad.LogisticsApp.mappers;

import com.zavrsniRad.LogisticsApp.domain.dto.crew.CrewMemberResponse;
import com.zavrsniRad.LogisticsApp.domain.models.crew.CrewMember;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = UserMapper.class)
public interface CrewMemberMapper {

    CrewMemberResponse toResponse(CrewMember crewMember);

}
