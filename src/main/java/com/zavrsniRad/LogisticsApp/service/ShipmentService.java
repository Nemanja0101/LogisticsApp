package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.shipment.ShipmentResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight.FreightResponse;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Freight;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Shipment;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.FreightMapper;
import com.zavrsniRad.LogisticsApp.mappers.ShipmentMapper;
import com.zavrsniRad.LogisticsApp.repositories.FreightRepository;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    //only admins and dispetchers can access this service via controller guard
    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final FreightRepository freightRepository;
    private final FreightMapper freightMapper;

    @Transactional
    public List<ShipmentResponse> getAllShipments() {
        List<Shipment> allShipments = shipmentRepository.findAllWithFreights();
        return allShipments.stream().map(shipmentMapper::toResponse).toList();
    }

    @Transactional
    public ShipmentResponse getShipment(UUID id) {
        Shipment shipment = shipmentRepository.findByIdAndFetchFreights(id)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find shipment with ID:" + id));
        return shipmentMapper.toResponse(shipment);
    }

    public ShipmentResponse createShipment() {
        Shipment newShipment = Shipment.builder().build();
        Shipment savedShipment = shipmentRepository.save(newShipment);
        return shipmentMapper.toResponse(savedShipment);
    }

    @Transactional
    public void deleteShipment(UUID id) {

        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find shipment with ID:" + id));
        shipmentRepository.delete(shipment);

    }

    @Transactional
    public List<FreightResponse> getFreightsOnShipment(UUID id) {

        shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find shipment with ID:" + id));
        List<Freight> allByShipmentId = freightRepository.findAllByShipmentId(id);

        return allByShipmentId.stream().map(freightMapper::toResponse).toList();
    }
}
