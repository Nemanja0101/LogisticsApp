package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.crew.CreateCrewAssignment;
import com.zavrsniRad.LogisticsApp.domain.dto.crew.CrewAssignmentResponse;
import com.zavrsniRad.LogisticsApp.domain.enums.ShipmentOrderStatus;
import com.zavrsniRad.LogisticsApp.domain.models.crew.CrewAssignment;
import com.zavrsniRad.LogisticsApp.domain.models.crew.CrewMember;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Freight;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Shipment;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentSegment;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.CrewAssignmentMapper;
import com.zavrsniRad.LogisticsApp.repositories.CrewAssignmentRepository;
import com.zavrsniRad.LogisticsApp.repositories.CrewMemberRepository;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentRepository;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CrewAssignmentService {

    private static final Set<ShipmentOrderStatus> CREW_CHANGEABLE_STATUSES = EnumSet.of(
            ShipmentOrderStatus.PENDING,
            ShipmentOrderStatus.CONFIRMED,
            ShipmentOrderStatus.ASSIGNED,
            ShipmentOrderStatus.ON_HOLD,
            ShipmentOrderStatus.CANCELLED
    );

    private final CrewAssignmentRepository crewAssignmentRepository;
    private final CrewAssignmentMapper crewAssignmentMapper;
    private final ShipmentSegmentRepository shipmentSegmentRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final ShipmentRepository shipmentRepository;
    private final TransportCompatibilityChecker transportCompatibilityChecker;

    public List<CrewAssignmentResponse> getAssignmentsForCrewMember(UUID id) {

        crewMemberRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crew member with the provided ID: " + id + " doesn't exist"));

        List<CrewAssignment> allAssignments = crewAssignmentRepository.findAllByCrewMemberId(id);
        return allAssignments.stream().map(crewAssignmentMapper::toResponse).toList();

    }

    public List<CrewAssignmentResponse> getAssignmentsForSegment(UUID shipmentId, UUID id) {

        shipmentSegmentRepository.findByIdAndShipmentId(id, shipmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Shipment segment with ID: " + id + " doesn't exist on a shipment with ID: " + shipmentId));

        List<CrewAssignment> allOnShipmentSegment = crewAssignmentRepository.findAllByShipmentSegmentId(id);
        return allOnShipmentSegment.stream().map(crewAssignmentMapper::toResponse).toList();

    }

    public CrewAssignmentResponse getCrewAssignment(UUID id) {

        CrewAssignment crewAssignment = crewAssignmentRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crew assignment with the provided ID: " + id + " doesn't exist"));

        return crewAssignmentMapper.toResponse(crewAssignment);
    }

    @Transactional
    public CrewAssignmentResponse createCrewAssignment(CreateCrewAssignment createCrewAssignment) {

        CrewMember crewMember = crewMemberRepository.findWithUserById(createCrewAssignment.getCrewMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Crew member with the provied ID: " + createCrewAssignment.getCrewMemberId() + " doesn't exist"));

        ShipmentSegment shipmentSegment = shipmentSegmentRepository.findById(createCrewAssignment.getShipmentSegmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipment segment with the provided ID: "+  createCrewAssignment.getShipmentSegmentId()  + " doesn't exist"));

        Shipment shipment = shipmentRepository
                .findByIdAndFetchFreightsAndShipmentOrder(shipmentSegment.getShipment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipment with the provided ID: "
                        + shipmentSegment.getShipment().getId() + " doesn't exist"));

        if (crewAssignmentRepository.existsByCrewMemberIdAndShipmentSegmentId(
                createCrewAssignment.getCrewMemberId(), createCrewAssignment.getShipmentSegmentId())) {
            throw new BusinessRuleViolationException("Crew member is already assigned to this segment", new ArrayList<>());
        }

        if (crewMember.getLicense().getExpiresAt().isBefore(LocalDate.now())) {
            throw new BusinessRuleViolationException("Crew member's license has expired", new ArrayList<>());
        }

        transportCompatibilityChecker.assertCrewMemberIsAdrCertified(crewMember.getLicense(), shipment.getFreights());

        transportCompatibilityChecker
               .assertCrewMemberLicenseValidForSegmentTransportType(
                       crewMember.getLicense().getLicenseType(),
                       shipmentSegment);

        CrewAssignment crewAssignment = CrewAssignment
                .builder()
                .crewMember(crewMember)
                .shipmentSegment(shipmentSegment)
                .build();

        CrewAssignment saved = crewAssignmentRepository.save(crewAssignment);

        return crewAssignmentMapper.toResponse(saved);
    }

    @Transactional
    public void deleteCrewAssignment(UUID id) {

        CrewAssignment crewAssignment = crewAssignmentRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crew assignment with the provided ID: " + id + " doesn't exist"));

        Shipment shipment = shipmentRepository
                .findByIdAndFetchFreightsAndShipmentOrder(crewAssignment.getShipmentSegment().getShipment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipment with the provided ID: "
                        + crewAssignment.getShipmentSegment().getShipment().getId() + " doesn't exist"));

        for (Freight freight : shipment.getFreights()) {

            ShipmentOrderStatus status = freight.getShipmentOrder().getStatus();

            if (!CREW_CHANGEABLE_STATUSES.contains(status)) {
                throw new BusinessRuleViolationException(
                        "Crew can't be unassigned while order " + freight.getShipmentOrder().getId() + " is in status " + status,
                        CREW_CHANGEABLE_STATUSES.stream().map(Enum::name).toList()
                );
            }
        }

        crewAssignmentRepository.delete(crewAssignment);

    }
}
