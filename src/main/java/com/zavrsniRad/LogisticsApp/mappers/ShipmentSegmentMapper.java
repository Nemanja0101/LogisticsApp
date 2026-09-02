package com.zavrsniRad.LogisticsApp.mappers;

import com.zavrsniRad.LogisticsApp.domain.dto.shipment.segment.ShipmentSegmentResponse;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentSegment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {LocationMapper.class, TransportUnitMapper.class})
public interface ShipmentSegmentMapper {

    @Mapping(target = "shipmentId", source = "shipment.id")
    ShipmentSegmentResponse toResponse(ShipmentSegment shipmentSegment);
}
