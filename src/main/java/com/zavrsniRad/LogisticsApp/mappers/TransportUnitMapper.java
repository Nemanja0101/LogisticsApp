package com.zavrsniRad.LogisticsApp.mappers;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.TransportUnitResponse;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransportUnitMapper {

    @Mapping(target = "identifier", source=".")
    TransportUnitResponse toResponse(TransportUnit transportUnit);

    default String toIdentifier(TransportUnit unit){

        return switch (unit){
            case Truck t -> t.getLicensePlate();
            case Van v -> v.getLicensePlate();
            case Vessel v  -> v.getImoNumber();
            case Railcar r -> r.getWagonNumber();
            case Aircraft a -> a.getRegistrationNumber();
            default        -> null;
        };

    }
}
