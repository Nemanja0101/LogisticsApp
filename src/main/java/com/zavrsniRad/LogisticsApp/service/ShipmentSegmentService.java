package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.shipment.segment.CreateUpdateShipmentSegment;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.segment.ShipmentSegmentResponse;
import com.zavrsniRad.LogisticsApp.domain.models.crew.CrewAssignment;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Freight;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Location;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Shipment;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentSegment;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.TransportUnit;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.ShipmentSegmentMapper;
import com.zavrsniRad.LogisticsApp.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentSegmentService {

    private final ShipmentSegmentRepository shipmentSegmentRepository;
    private final CrewAssignmentRepository crewAssignmentRepository;
    private final TransportCompatibilityChecker transportCompatibilityChecker;
    private final ShipmentRepository shipmentRepository;
    private final ShipmentSegmentMapper shipmentSegmentMapper;
    private final TransportUnitRepository transportUnitRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public List<ShipmentSegmentResponse> getAllShipmentSegments(UUID shipmentId) {

        if (!shipmentRepository.existsById(shipmentId)) {
            throw new ResourceNotFoundException("Shipment with ID: " + shipmentId + " doesn't exist");
        }

        List<ShipmentSegment> allShipmentSegments = shipmentSegmentRepository.findAllByShipmentId(shipmentId);
        return allShipmentSegments.stream().map(shipmentSegmentMapper::toResponse).toList();

    }


    @Transactional
    public ShipmentSegmentResponse getShipmentSegment(UUID shipmentId, UUID id) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment with ID: " + shipmentId + " doesn't exist"));


       ShipmentSegment shipmentSegment =
                shipmentSegmentRepository.findByIdAndShipmentId(id, shipmentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Shipment segment with ID: " + id + " and shipment ID: " + shipmentId + " doesn't exist"));


        return shipmentSegmentMapper.toResponse(shipmentSegment);
    }

    @Transactional
    public ShipmentSegmentResponse createShipmentSegment(UUID shipmentId,CreateUpdateShipmentSegment createShipmentSegment) {

        if (createShipmentSegment.getStartLocationId().equals(createShipmentSegment.getEndLocationId())) {
            throw new BusinessRuleViolationException(
                    "A segment must start and end at different locations",
                    new ArrayList<>()
            );
        }

        Shipment shipment = shipmentRepository.findByIdAndFetchFreightsAndShipmentOrder(shipmentId)
                .orElseThrow(()-> new ResourceNotFoundException("Shipment with the ID: " + shipmentId + " doesn't exist"));

        TransportUnit transportUnit = transportUnitRepository.findById(createShipmentSegment.getTransportUnitId())
                .orElseThrow(()-> new ResourceNotFoundException("Transport unit with the ID: " + createShipmentSegment.getTransportUnitId() + " doesn't exist"));

        Location startLocation = locationRepository.findById(createShipmentSegment.getStartLocationId())
                .orElseThrow(()-> new ResourceNotFoundException("Location unit with the ID: " + createShipmentSegment.getStartLocationId() + " doesn't exist"));

        Location endLocation = locationRepository.findById(createShipmentSegment.getEndLocationId())
                .orElseThrow(()-> new ResourceNotFoundException("Location unit with the ID: " + createShipmentSegment.getEndLocationId() + " doesn't exist"));


        List<ShipmentSegment> existingSegments = shipmentSegmentRepository.findAllByShipmentId(shipmentId);

        ShipmentSegment last = null;

        for (ShipmentSegment segment : existingSegments) {
            if (last == null || segment.getSequenceNumber() > last.getSequenceNumber()) {
                last = segment;
            }
        }

        if (last == null && !shipment.getFreights().isEmpty()){

            boolean matchesAnOrderStart = false;

            for (Freight freight: shipment.getFreights()){
                if (freight.getShipmentOrder().getStartLocation().getId()
                        .equals(createShipmentSegment.getStartLocationId())){
                    matchesAnOrderStart = true;
                    break;
                }
            }

            if (!matchesAnOrderStart) {
                throw new BusinessRuleViolationException(
                        "The first leg must start at the pickup location of at least one order on this shipment",
                        new ArrayList<>()
                );
            }

        }

        short expectedSequence = (last == null) ? 1 : (short) (last.getSequenceNumber() + 1);

        if (createShipmentSegment.getSequenceNumber() != expectedSequence) {
            throw new BusinessRuleViolationException(
                    "Segments must be added in order; expected sequence number " + expectedSequence,
                    new ArrayList<>()
            );
        }

        if (last != null && !last.getEndLocation().getId().equals(createShipmentSegment.getStartLocationId())) {
            throw new BusinessRuleViolationException(
                    "Segment must start where the previous one ended",
                    new ArrayList<>(List.of(last.getEndLocation().getId().toString()))
            );
        }

        transportCompatibilityChecker.assertUnitCanCarry(transportUnit, shipment.getFreights());

        ShipmentSegment segment = ShipmentSegment.builder()
                .shipment(shipment)
                .sequenceNumber(createShipmentSegment.getSequenceNumber())
                .transportUnit(transportUnit)
                .startLocation(startLocation)
                .endLocation(endLocation)
                .build();

        transportCompatibilityChecker.assertCapacity(List.of(segment), shipment.getFreights());

        ShipmentSegment saved = shipmentSegmentRepository.save(segment);

        return shipmentSegmentMapper.toResponse(saved);

    }

    @Transactional
    public ShipmentSegmentResponse updateShipmentSegment(UUID shipmentId, UUID id,CreateUpdateShipmentSegment updateShipmentSegment) {

        if (updateShipmentSegment.getStartLocationId().equals(updateShipmentSegment.getEndLocationId())) {
            throw new BusinessRuleViolationException(
                    "A segment must start and end at different locations",
                    new ArrayList<>()
            );
        }

        Shipment shipment = shipmentRepository.findByIdAndFetchFreightsAndShipmentOrder(shipmentId)
                .orElseThrow(()-> new ResourceNotFoundException("Shipment with the ID: " + shipmentId + " doesn't exist"));

        TransportUnit transportUnit = transportUnitRepository.findById(updateShipmentSegment.getTransportUnitId())
                .orElseThrow(()-> new ResourceNotFoundException("Transport unit with the ID: " + updateShipmentSegment.getTransportUnitId() + " doesn't exist"));

        Location startLocation = locationRepository.findById(updateShipmentSegment.getStartLocationId())
                .orElseThrow(()-> new ResourceNotFoundException("Location unit with the ID: " + updateShipmentSegment.getStartLocationId() + " doesn't exist"));

        Location endLocation = locationRepository.findById(updateShipmentSegment.getEndLocationId())
                .orElseThrow(()-> new ResourceNotFoundException("Location unit with the ID: " + updateShipmentSegment.getEndLocationId() + " doesn't exist"));

        ShipmentSegment current = shipmentSegmentRepository.findByIdAndShipmentId(id, shipmentId)
                .orElseThrow(()-> new ResourceNotFoundException("Shipment segment with the provided ID: "+ id + " and shipment ID: " + shipmentId + " doesn't exist"));

        Optional<ShipmentSegment> previous = shipmentSegmentRepository
                .findByShipmentIdAndSequenceNumber(shipmentId, (short) (current.getSequenceNumber() - 1));

        Optional<ShipmentSegment> next = shipmentSegmentRepository
                .findByShipmentIdAndSequenceNumber(shipmentId, (short) (current.getSequenceNumber() + 1));

        if(previous.isEmpty() && !shipment.getFreights().isEmpty()){

            boolean matchesAnOrderStart = false;

            for (Freight freight: shipment.getFreights()){
                if (freight.getShipmentOrder().getStartLocation().getId()
                        .equals(updateShipmentSegment.getStartLocationId())){
                    matchesAnOrderStart = true;
                    break;
                }
            }

            if (!matchesAnOrderStart) {
                throw new BusinessRuleViolationException(
                        "The first leg must start at the pickup location of at least one order on this shipment",
                        new ArrayList<>()
                );
            }
        }

        if (
                previous.isPresent() &&
                !previous.get().getEndLocation().getId().equals(startLocation.getId()))
        {
            throw new BusinessRuleViolationException("Start location of the new segment doesn't match the end location of the previous segment",
                    new ArrayList<>(
                            List.of(
                                    startLocation.getName(),
                                    startLocation.getAddress(),
                                    startLocation.getCity())));
        }


        if (
                next.isPresent() &&
                        !next.get().getStartLocation().getId().equals(endLocation.getId()))
        {
            throw new BusinessRuleViolationException("End location of the new segment doesn't match the start location of the next segment",
                    new ArrayList<>(
                            List.of(
                                    endLocation.getName(),
                                    endLocation.getAddress(),
                                    endLocation.getCity())));
        }

        short expectedSequence = previous.isEmpty() ? 1 : (short) (previous.get().getSequenceNumber() + 1);

        if (updateShipmentSegment.getSequenceNumber() != expectedSequence) {
            throw new BusinessRuleViolationException(
                    "Segments must be added in order; expected sequence number " + expectedSequence,
                    new ArrayList<>()
            );
        }

        transportCompatibilityChecker.assertUnitCanCarry(transportUnit, shipment.getFreights());

        current.setEndLocation(endLocation);
        current.setStartLocation(startLocation);
        current.setSequenceNumber(updateShipmentSegment.getSequenceNumber());
        current.setTransportUnit(transportUnit);

        for (CrewAssignment assignment : crewAssignmentRepository.findAllByShipmentSegmentId(id)){
            transportCompatibilityChecker.assertCrewMemberLicenseValidForSegmentTransportType(
                    assignment.getCrewMember().getLicense().getLicenseType(), current
            );
        }

        transportCompatibilityChecker.assertCapacity(List.of(current), shipment.getFreights());

        ShipmentSegment saved = shipmentSegmentRepository.save(current);

        return shipmentSegmentMapper.toResponse(saved);
    }

    @Transactional
    public void deleteShipmentSegment(UUID shipmentId, UUID id) {

        ShipmentSegment current = shipmentSegmentRepository.findByIdAndShipmentId(id, shipmentId)
                .orElseThrow(()-> new ResourceNotFoundException("Shipment segment with the provided ID: "+ id + " and shipment ID: " + shipmentId + " doesn't exist"));

        Optional<ShipmentSegment> next = shipmentSegmentRepository
                .findByShipmentIdAndSequenceNumber(shipmentId, (short) (current.getSequenceNumber() + 1));

        if (next.isPresent()){
            throw new BusinessRuleViolationException("You can't delete a shipment segment in the middle of shipment, as it breaks the contiguity, rather try updating a whole segment", new ArrayList<>());
        }

        List<CrewAssignment> assignments = crewAssignmentRepository.findAllByShipmentSegmentId(id);

        if (!assignments.isEmpty()){
            throw new BusinessRuleViolationException(
                    "You must unassign all crew from this segment before deleting it",
                    assignments.stream()
                            .map(assignment -> assignment.getCrewMember().getUser().getFirstName() + " "
                                    + assignment.getCrewMember().getUser().getLastName()
                                    + " (" + assignment.getCrewMember().getId() + ")")
                            .toList()
            );
        }

        shipmentSegmentRepository.delete(current);

    }
}
