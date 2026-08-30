package com.zavrsniRad.LogisticsApp.controllers.shipment;


import com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight.CreateFreight;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight.FreightResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight.PatchFreight;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.order.CreateUpdateShipmentOrder;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.order.PatchShipmentOrder;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.order.ShipmentOrderResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.order.UpdateShipmentOrderStatus;
import com.zavrsniRad.LogisticsApp.service.FreightService;
import com.zavrsniRad.LogisticsApp.service.ShipmentOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shipment-orders")
@RequiredArgsConstructor
public class ShipmentOrderController {

    private final ShipmentOrderService shipmentOrderService;
    private final FreightService freightService;


    @GetMapping()
    public ResponseEntity<List<ShipmentOrderResponse>> getAllMyShipmentOrders(){

        List<ShipmentOrderResponse> allShipmentOrders = shipmentOrderService.getAllMyShipmentOrders();

        return ResponseEntity.status(HttpStatus.OK).body(allShipmentOrders);

    }

    @GetMapping(path = "/admin")
    @PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
    public ResponseEntity<List<ShipmentOrderResponse>> getAllShipmentOrders(){

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
            @Valid @RequestBody PatchShipmentOrder updateShipmentOrder
    ) {

        ShipmentOrderResponse updatedShipmentOrder =
                shipmentOrderService.patchShipmentOrder(id, updateShipmentOrder);

        return ResponseEntity.status(HttpStatus.OK).body(updatedShipmentOrder);
    }


    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
    public ResponseEntity<ShipmentOrderResponse> updateShipmentOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateShipmentOrderStatus request
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


    @PutMapping(path = "/{orderId}/freights/{freightId}")
    public ResponseEntity<FreightResponse> updateFreightOnShipmentOrder(
            @PathVariable UUID orderId,
            @PathVariable UUID freightId,
            @Valid @RequestBody CreateFreight updateFreightRequest
    ){

        FreightResponse response =
                freightService.updateFreightOnShipmentOrder(orderId, freightId, updateFreightRequest);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }


    @PatchMapping(path = "/{orderId}/freights/{freightId}")
    public ResponseEntity<FreightResponse> patchFreightOnShipmentOrder(
            @PathVariable UUID orderId,
            @PathVariable UUID freightId,
            @Valid @RequestBody PatchFreight patchFreightRequest
    ){

        FreightResponse response =
                freightService.patchFreightOnShipmentOrder(orderId, freightId, patchFreightRequest);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }


    @DeleteMapping(path = "/{orderId}/freights/{freightId}")
    public ResponseEntity<Void> deleteFreightOnShipmentOrder(
            @PathVariable UUID orderId,
            @PathVariable UUID freightId
    ){

        freightService.deleteFreightOnShipmentOrder(orderId, freightId);
        return ResponseEntity.noContent().build();

    }
}
