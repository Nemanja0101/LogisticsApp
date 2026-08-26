package com.zavrsniRad.LogisticsApp.controllers.shipment;


import com.zavrsniRad.LogisticsApp.domain.dto.shipment.*;
import com.zavrsniRad.LogisticsApp.service.FreightService;
import com.zavrsniRad.LogisticsApp.service.ShipmentOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shipment-orders")
@RequiredArgsConstructor
public class ShipmentOrderController {

    private final ShipmentOrderService shipmentOrderService;
    private final FreightService freightService;

    @GetMapping
    public ResponseEntity<?> getAllShipmentOrders(){

        List<ShipmentOrderResponse> allShipmentOrders = shipmentOrderService.getAllShipmentOrders();

        return ResponseEntity.status(HttpStatus.OK).body(allShipmentOrders);

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ShipmentOrderResponse> getShipmentOrder(@PathVariable UUID id) {


        ShipmentOrderResponse shipmentOrderResponse = shipmentOrderService.getShipmentOrder(id);

        return ResponseEntity.status(HttpStatus.OK).body(shipmentOrderResponse);

    }

    @PostMapping
    public ResponseEntity<?> createShipmentOrder(
            @Valid @RequestBody CreateUpdateShipmentOrder createUpdateShipmentOrder
            ){

        ShipmentOrderResponse createdShipmentOrder = shipmentOrderService.createShipmentOrder(createUpdateShipmentOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdShipmentOrder);
    }


// new methods

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentOrderResponse> updateShipmentOrder(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUpdateShipmentOrder updateShipmentOrder
    ) {

        ShipmentOrderResponse updatedShipmentOrder =
                shipmentOrderService.updateShipmentOrder(id, updateShipmentOrder);

        return ResponseEntity.ok(updatedShipmentOrder);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ShipmentOrderResponse> patchShipmentOrder(
            @PathVariable UUID id,
            @RequestBody PatchShipmentOrder updateShipmentOrder
    ) {

        ShipmentOrderResponse updatedShipmentOrder =
                shipmentOrderService.patchShipmentOrder(id, updateShipmentOrder);

        return ResponseEntity.status(HttpStatus.OK).body(updatedShipmentOrder);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<ShipmentOrderResponse> updateShipmentOrderStatus(
            @PathVariable UUID id,
            @RequestBody UpdateShipmentOrderStatus request
    ) {

        ShipmentOrderResponse updatedShipmentOrder =
                shipmentOrderService.updateShipmentOrderStatus(id, request);

        return ResponseEntity.ok(updatedShipmentOrder);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentOrder(
            @PathVariable UUID id
    ) {

        shipmentOrderService.deleteShipmentOrder(id);

        return ResponseEntity.noContent().build();
    }

//end of new methods
    @PostMapping(path = "/{orderId}/freights")
    public ResponseEntity<?> createFright(
            @PathVariable UUID orderId,
            @Valid @RequestBody CreateFreight createFreightRequest
    ){

        FreightResponse createdFreight = freightService.createFreight(createFreightRequest, orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFreight);

    }


    @GetMapping(path = "/{orderId}/freights")
    public ResponseEntity<?> getAllFreightsOnShipmentOrder(
            @PathVariable UUID orderId
    ){

        List<FreightResponse> freightResponseList = freightService.getAllFreightsOnShipmentOrder(orderId);

        return ResponseEntity.status(HttpStatus.OK).body(freightResponseList);

    }

    @GetMapping(path = "/{orderId}/freights/{freightId}")
    public ResponseEntity<?> getSingleFreightOnShipmentOrder(
            @PathVariable UUID orderId,
            @PathVariable UUID freightId
    ){


        FreightResponse response = freightService.getSingleFreightOnShipmentOrder(orderId, freightId);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }


/*
    FINISHED:
    GET  /api/v1/shipment-orders                        -> GETS ALL SHIPMENT ORDERS IN THE DB
    GET /api/v1/shipment-orders/{id}                    -> GETS THE SPECIFIC SHIPMENT ORDER WITH THE ID
    POST /api/v1/shipment-orders                        -> CREATES A SINGLE SHIPMENT ORDER
    POST /api/v1/shipment-orders/{orderId}/freights     -> CREATE A SINGLE FREIGHT
    GET  /api/v1/shipment-orders/{orderId}/freights     -> GET ALL FREIGHTS ON A SHIPMENT ORDER
    GET  /api/v1/shipment-orders/{orderId}/freights/{freightId} -> GET A SINGLE FREIGHT
    PUT /api/v1/shipment-orders/{id}        -> FULL UPDATE / REPLACE THE EVENT WITH THE EVENT BODY PROVIDED
    PATCH  /api/v1/shipment-orders/{id}     -> partial update
        PATCH /api/v1/shipment-orders/{id}/status   body: { "status": "CONFIRMED" }
    DELETE /api/v1/shipment-orders/{id}     -> DELETES A SHIPMENT ORDER




    */










}
