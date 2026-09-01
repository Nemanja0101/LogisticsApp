package com.zavrsniRad.LogisticsApp.mappers;


import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateTruck;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.TruckResponse;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Truck;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TruckMapper {

    TruckResponse toResponse(Truck truck);
    Truck toEntity(CreateUpdateTruck createUpdateTruck);

}
