package com.zavrsniRad.LogisticsApp.mappers;


import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.AircraftResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateAircraft;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Aircraft;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AircraftMapper {

    AircraftResponse toResponse(Aircraft aircraft);
    Aircraft toEntity(CreateUpdateAircraft createUpdateAircraft);
}
