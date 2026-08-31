package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.shipment.ShipmentResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight.AssignFreight;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight.FreightResponse;
import com.zavrsniRad.LogisticsApp.domain.enums.ShipmentOrderStatus;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Freight;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Shipment;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentOrder;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.FreightMapper;
import com.zavrsniRad.LogisticsApp.mappers.ShipmentMapper;
import com.zavrsniRad.LogisticsApp.repositories.FreightRepository;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ShipmentService {


    private static final Set<ShipmentOrderStatus> FREIGHT_ASSIGNABLE_STATUS = EnumSet.of(
            ShipmentOrderStatus.PENDING,
            ShipmentOrderStatus.CONFIRMED,
            ShipmentOrderStatus.ON_HOLD
    );

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

//            if shipment has freight
//        you cant delete

        Shipment shipment = shipmentRepository.findByIdAndFetchFreightsAndShipmentOrder(id)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find shipment with ID:" + id));

        if (!shipment.getFreights().isEmpty()){

            List<String> freightStrings = shipment.getFreights().stream().map(freight -> freight.getId() + ": " + freight.getName() + " order:" + freight.getShipmentOrder().getId()).toList();

            throw new
                    BusinessRuleViolationException("You must remove all freights from the shipment in order to delete it", freightStrings);
        }

        shipmentRepository.delete(shipment);

    }

    @Transactional
    public List<FreightResponse> getFreightsOnShipment(UUID id) {

        shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find shipment with ID:" + id));
        List<Freight> allByShipmentId = freightRepository.findAllByShipmentId(id);

        return allByShipmentId.stream().map(freightMapper::toResponse).toList();
    }

    @Transactional
    public ShipmentResponse assignFreightToShipment(UUID id, AssignFreight assignFreightRequest) {

        //check if freights exists
        //check if shipment exists
        Freight freight = freightRepository.findByIdWithShipmentOrder(assignFreightRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Freight with ID:" + assignFreightRequest.getId() + " doesn't exist"));

        if (freight.getShipment() != null) {
            throw new BusinessRuleViolationException(
                    "Freight is already assigned to shipment " + freight.getShipment().getId(),
                    new ArrayList<>()
            );
        }

//        if i try to assign freight but the shipment is actually in progress, it shouldn't be allowed
        assertFreightAssignable(freight.getShipmentOrder());

        Shipment shipment = shipmentRepository.findByIdAndFetchFreights(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment with ID:" + id + " doesn't exist"));

        freight.setShipment(shipment);
        freightRepository.save(freight);
        shipment.getFreights().add(freight);

        Shipment saved = shipmentRepository.save(shipment);

        return shipmentMapper.toResponse(saved);
    }

    @Transactional
    public void unassignFreightFromShipment(UUID id, UUID freightId) {

        Freight freight = freightRepository.findByIdWithShipmentOrderAndShipment(freightId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Freight with ID: " + freightId + " is not connected to shipment with ID: " + id));
        assertFreightAssignable(freight.getShipmentOrder());
        freight.setShipment(null);
        freightRepository.save(freight);
    }

    private void assertFreightAssignable(ShipmentOrder shipmentOrder){

        ShipmentOrderStatus status = shipmentOrder.getStatus();

        if (!FREIGHT_ASSIGNABLE_STATUS.contains(status)){
            throw new BusinessRuleViolationException(
                    "Freight can't be assigned while the order is in status " + status,
                    FREIGHT_ASSIGNABLE_STATUS.stream().map(Enum::name).toList()
            );
        }

    }
}
