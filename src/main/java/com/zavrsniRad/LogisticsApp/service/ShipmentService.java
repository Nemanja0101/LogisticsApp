package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.shipment.ShipmentResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight.AssignFreight;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight.FreightResponse;
import com.zavrsniRad.LogisticsApp.domain.enums.ShipmentOrderStatus;
import com.zavrsniRad.LogisticsApp.domain.models.crew.CrewAssignment;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Freight;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Shipment;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentOrder;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentSegment;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.FreightMapper;
import com.zavrsniRad.LogisticsApp.mappers.ShipmentMapper;
import com.zavrsniRad.LogisticsApp.repositories.CrewAssignmentRepository;
import com.zavrsniRad.LogisticsApp.repositories.FreightRepository;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentRepository;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final FreightRepository freightRepository;
    private final FreightMapper freightMapper;
    private final ShipmentSegmentRepository shipmentSegmentRepository;
    private final CrewAssignmentRepository crewAssignmentRepository;
    private final TransportCompatibilityChecker transportCompatibilityChecker;

    @Transactional
    public Page<ShipmentResponse> getAllShipments(Pageable pageable) {

        return shipmentRepository.findAll(pageable).map(shipmentMapper::toResponse);

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

        Freight freight = freightRepository.findByIdWithShipmentOrder(assignFreightRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Freight with ID:" + assignFreightRequest.getId() + " doesn't exist"));

        if (freight.getShipment() != null) {
            throw new BusinessRuleViolationException(
                    "Freight is already assigned to shipment " + freight.getShipment().getId(),
                    new ArrayList<>()
            );
        }
        Shipment shipment = shipmentRepository.findByIdAndFetchFreights(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment with ID:" + id + " doesn't exist"));

        List<ShipmentSegment> segments = shipmentSegmentRepository.findAllByShipmentId(id);

        transportCompatibilityChecker.assertCapacity(segments, shipment.getFreights(), freight);

        for (ShipmentSegment segment : segments) {
            transportCompatibilityChecker.assertUnitCanCarry(segment.getTransportUnit(), List.of(freight));
        }

        List<UUID> segmentIds = segments.stream().map(ShipmentSegment::getId).toList();

        if (!segmentIds.isEmpty()) {

            for (CrewAssignment assignment : crewAssignmentRepository.findAllByShipmentSegmentIdIn(segmentIds)) {
                transportCompatibilityChecker.assertCrewMemberIsDangerousGoodsCertified(
                        assignment.getCrewMember().getLicense(),
                        List.of(freight)
                );
            }

        }

        assertFreightAssignable(freight.getShipmentOrder());


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
