package com.zavrsniRad.LogisticsApp.mappers;


import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateVan;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.VanResponse;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Van;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VanMapper {

    VanResponse toResponse(Van van);
    Van toVan(CreateUpdateVan createUpdateVan);

}
