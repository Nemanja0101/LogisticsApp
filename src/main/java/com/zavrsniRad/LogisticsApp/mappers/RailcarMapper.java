package com.zavrsniRad.LogisticsApp.mappers;


import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateRailcar;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.RailcarResponse;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Railcar;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RailcarMapper {

    RailcarResponse toResponse(Railcar railcar);
    Railcar toRailcar(CreateUpdateRailcar createUpdateRailcar);

}
