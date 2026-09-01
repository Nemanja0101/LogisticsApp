package com.zavrsniRad.LogisticsApp.mappers;


import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateVessel;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.VesselResponse;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Vessel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VesselMapper {

    VesselResponse toResponse(Vessel vessel);
    Vessel toVessel(CreateUpdateVessel createUpdateVessel);

}
