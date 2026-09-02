package com.zavrsniRad.LogisticsApp.controllers.shipment;


import com.zavrsniRad.LogisticsApp.domain.dto.crew.CrewAssignmentResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.segment.CreateUpdateShipmentSegment;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.segment.ShipmentSegmentResponse;
import com.zavrsniRad.LogisticsApp.service.CrewAssignmentService;
import com.zavrsniRad.LogisticsApp.service.ShipmentSegmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shipments/{shipmentId}/segments")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
public class ShipmentSegmentController {

    private final ShipmentSegmentService shipmentSegmentService;
    private final CrewAssignmentService crewAssignmentService;


    @GetMapping
    public ResponseEntity<List<ShipmentSegmentResponse>> getAllShipmentSegments(
            @PathVariable UUID shipmentId
    ){

        List<ShipmentSegmentResponse> segments = shipmentSegmentService.getAllShipmentSegments(shipmentId);
        return ResponseEntity.status(HttpStatus.OK).body(segments);

    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<ShipmentSegmentResponse> getShipmentSegment(
            @PathVariable UUID shipmentId,
            @PathVariable UUID id
    ){

        ShipmentSegmentResponse segment = shipmentSegmentService.getShipmentSegment(shipmentId, id);
        return ResponseEntity.status(HttpStatus.OK).body(segment);

    }


    @GetMapping(path = "/{id}/crew")
    public ResponseEntity<List<CrewAssignmentResponse>> getCrewOnSegment(
            @PathVariable UUID shipmentId,
            @PathVariable UUID id
    ){

        List<CrewAssignmentResponse> assignments = crewAssignmentService.getAssignmentsForSegment(shipmentId, id);
        return ResponseEntity.status(HttpStatus.OK).body(assignments);

    }


    @PostMapping
    public ResponseEntity<ShipmentSegmentResponse> createShipmentSegment(
            @PathVariable UUID shipmentId,
            @Valid @RequestBody CreateUpdateShipmentSegment createShipmentSegment
    ){

        ShipmentSegmentResponse segment =
                shipmentSegmentService.createShipmentSegment(shipmentId, createShipmentSegment);

        return ResponseEntity.status(HttpStatus.CREATED).body(segment);

    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<ShipmentSegmentResponse> updateShipmentSegment(
            @PathVariable UUID shipmentId,
            @PathVariable UUID id,
            @Valid @RequestBody CreateUpdateShipmentSegment updateShipmentSegment
    ){

        ShipmentSegmentResponse segment =
                shipmentSegmentService.updateShipmentSegment(shipmentId, id, updateShipmentSegment);

        return ResponseEntity.status(HttpStatus.OK).body(segment);

    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteShipmentSegment(
            @PathVariable UUID shipmentId,
            @PathVariable UUID id
    ){

        shipmentSegmentService.deleteShipmentSegment(shipmentId, id);
        return ResponseEntity.noContent().build();

    }

}
