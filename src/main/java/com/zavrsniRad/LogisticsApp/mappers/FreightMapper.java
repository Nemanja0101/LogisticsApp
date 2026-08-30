package com.zavrsniRad.LogisticsApp.mappers;

import com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight.CreateFreight;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight.FreightResponse;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Freight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FreightMapper {


    @Mapping(target = "shipmentOrderId", source = "shipmentOrder.id")
    @Mapping(target = "shipmentId", source = "shipment.id")
    FreightResponse toResponse(Freight freight);

    Freight toEntity(CreateFreight createFreight);


}
