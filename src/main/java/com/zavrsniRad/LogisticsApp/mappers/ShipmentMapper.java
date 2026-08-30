package com.zavrsniRad.LogisticsApp.mappers;

import com.zavrsniRad.LogisticsApp.domain.dto.shipment.ShipmentResponse;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = FreightMapper.class)
public interface ShipmentMapper {

    ShipmentResponse toResponse(Shipment shipment);


}
