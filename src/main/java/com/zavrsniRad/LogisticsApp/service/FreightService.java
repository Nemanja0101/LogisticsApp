package com.zavrsniRad.LogisticsApp.service;


import com.zavrsniRad.LogisticsApp.domain.dto.shipment.CreateFreight;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.FreightResponse;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Freight;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentOrder;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.FreightMapper;
import com.zavrsniRad.LogisticsApp.repositories.FreightRepository;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FreightService {

    private final FreightRepository freightRepository;
    private final ShipmentOrderRepository shipmentOrderRepository;
    private final FreightMapper freightMapper;

    public FreightResponse createFreight(CreateFreight request, UUID orderId) {

        ShipmentOrder order = shipmentOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment order with id: " + orderId + " not found"));

        Freight newFreight = freightMapper.toEntity(request);
        newFreight.setShipmentOrder(order);
        Freight savedFreight = freightRepository.save(newFreight);

        return freightMapper.toResponse(savedFreight);

    }

    public List<FreightResponse> getAllFreightsOnShipmentOrder(UUID orderId) {

        shipmentOrderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Shipment with the ID:" + orderId + "doesn't exist")
        );


        return freightRepository.findAllByShipmentOrderId(orderId)
                .stream()
                .map(freightMapper::toResponse)
                .toList();


    }

    public FreightResponse getSingleFreightOnShipmentOrder(UUID orderId, UUID freightId) {

        Optional<Freight> oFreight = freightRepository.findByIdAndShipmentOrderId(freightId, orderId);

        if (oFreight.isPresent()){

            return freightMapper.toResponse(oFreight.get());

        }else{
            throw new ResourceNotFoundException("Freight with id: " + freightId + " was not found");
        }


    }
}
