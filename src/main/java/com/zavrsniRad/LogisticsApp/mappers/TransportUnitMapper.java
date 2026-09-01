package com.zavrsniRad.LogisticsApp.mappers;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.TransportUnitResponse;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.TransportUnit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransportUnitMapper {

    TransportUnitResponse toResponse(TransportUnit transportUnit);

}
