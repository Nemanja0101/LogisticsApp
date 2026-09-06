package com.zavrsniRad.LogisticsApp.mappers;


import com.zavrsniRad.LogisticsApp.domain.dto.shipment.order.ShipmentOrderResponse;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = LocationMapper.class)
public interface ShipmentOrderMapper {


    @Mapping(target = "customerId", source = "shipmentOrder.customer.id")
    @Mapping(target = "freightCount", source = "freightCount")
    ShipmentOrderResponse toResponse(ShipmentOrder shipmentOrder, long freightCount);

}
