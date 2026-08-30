package com.zavrsniRad.LogisticsApp.controllers.shipment;

import com.zavrsniRad.LogisticsApp.domain.dto.shipment.AssignFreight;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight.FreightResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.ShipmentResponse;
import com.zavrsniRad.LogisticsApp.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
public class ShipmentController {

    private final ShipmentService shipmentService;


    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getAllShipments(){

        List<ShipmentResponse> shipments = shipmentService.getAllShipments();
        return ResponseEntity.status(HttpStatus.OK).body(shipments);

    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<ShipmentResponse> getShipment(
            @PathVariable UUID id
    ){
        ShipmentResponse shipment = shipmentService.getShipment(id);
        return ResponseEntity.status(HttpStatus.OK).body(shipment);
    }

    @PostMapping
    public ResponseEntity<ShipmentResponse> createShipment(){

        ShipmentResponse shipment = shipmentService.createShipment();
        return ResponseEntity.status(HttpStatus.CREATED).body(shipment);

    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteShipment(
            @PathVariable UUID id
    ){
        shipmentService.deleteShipment(id);
        return ResponseEntity.noContent().build();

    }


    @GetMapping(path = "/{id}/freights")
    public ResponseEntity<List<FreightResponse>> getFreightsOnShipment(
            @PathVariable UUID id
    ){

        List<FreightResponse> freights = shipmentService.getFreightsOnShipment(id);
        return ResponseEntity.status(HttpStatus.OK).body(freights);

    }


    @PostMapping(path = "/{id}/freights")
    public ResponseEntity<ShipmentResponse> assignFreightToShipment(
            @PathVariable UUID id,
            @Valid @RequestBody AssignFreight assignFreightRequest
    ){

        ShipmentResponse shipment = shipmentService.assignFreightToShipment(id, assignFreightRequest);
        return ResponseEntity.status(HttpStatus.OK).body(shipment);

    }


    @DeleteMapping(path = "/{id}/freights/{freightId}")
    public ResponseEntity<Void> unassignFreightFromShipment(
            @PathVariable UUID id,
            @PathVariable UUID freightId
    ){

        shipmentService.unassignFreightFromShipment(id, freightId);
        return ResponseEntity.noContent().build();

    }

}
