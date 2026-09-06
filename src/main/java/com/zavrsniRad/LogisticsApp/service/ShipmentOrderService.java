package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.shipment.order.CreateUpdateShipmentOrder;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.order.PatchShipmentOrder;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.order.ShipmentOrderResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.order.UpdateShipmentOrderStatus;
import com.zavrsniRad.LogisticsApp.domain.enums.ShipmentOrderStatus;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Location;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentOrder;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.ShipmentOrderMapper;
import com.zavrsniRad.LogisticsApp.repositories.FreightRepository;
import com.zavrsniRad.LogisticsApp.repositories.LocationRepository;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentOrderRepository;
import com.zavrsniRad.LogisticsApp.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentOrderService {

    private final ShipmentOrderRepository shipmentOrderRepository;
    private final FreightRepository freightRepository;
    private final ShipmentOrderMapper shipmentOrderMapper;
    private final CurrentUserProvider currentUserProvider;
    private final LocationRepository locationRepository;


    public Page<ShipmentOrderResponse> getAllShipmentOrders(Pageable pageable){

        Page<ShipmentOrder> orders = shipmentOrderRepository.findAll(pageable);

        Map<UUID, Long> countsByOrderIds = freightCountsFor(orders.getContent());

        return orders.map(order -> shipmentOrderMapper.toResponse(
                order,
                countsByOrderIds.getOrDefault(order.getId(), 0L)
        ));

    }

    @Transactional
    public Page<ShipmentOrderResponse> getAllMyShipmentOrders(Pageable pageable) {

        User currentUser = currentUserProvider.getCurrentUser();

        Page<ShipmentOrder> orders = shipmentOrderRepository.findAllByCustomerId(currentUser.getId(), pageable);

        Map<UUID, Long> countsByOrderIds = freightCountsFor(orders.getContent());

        return orders.map(order -> shipmentOrderMapper.toResponse(
                order,
                countsByOrderIds.getOrDefault(order.getId(), 0L)
        ));

    }

    private Map<UUID, Long> freightCountsFor(List<ShipmentOrder> orders) {

        List<UUID> orderIds = orders.stream().map(ShipmentOrder::getId).toList();

        if (orderIds.isEmpty()) {
            return Map.of();
        }

        return freightRepository.countByShipmentOrderIds(orderIds)
                .stream()
                .collect(Collectors.toMap(
                        row -> (UUID) row[0],
                        row -> (Long) row[1]
                ));
    }


    @Transactional
    public ShipmentOrderResponse createShipmentOrder(CreateUpdateShipmentOrder createUpdateShipmentOrder) {

        Location startLocation =
                locationRepository.findById(createUpdateShipmentOrder.getStartLocationId())
                        .orElseThrow(() -> new ResourceNotFoundException("Can't find the location with the specified ID: " + createUpdateShipmentOrder.getStartLocationId()));
        Location endLocation = locationRepository.findById(createUpdateShipmentOrder.getEndLocationId())
                        .orElseThrow(() -> new ResourceNotFoundException("Can't find the location with the specified ID: " + createUpdateShipmentOrder.getEndLocationId()));

        ShipmentOrder newShipmentOrder = ShipmentOrder
                .builder()
                .startLocation(startLocation)
                .endLocation(endLocation)
                .status(ShipmentOrderStatus.PENDING)
                .customer(currentUserProvider.getCurrentUser())
                .build();


        ShipmentOrder saved = shipmentOrderRepository.save(newShipmentOrder);

        return shipmentOrderMapper.toResponse(saved, 0L);
    }

    public ShipmentOrderResponse getShipmentOrder(UUID id) {

        User currentUser = currentUserProvider.getCurrentUser();

        ShipmentOrder shipmentOrder = currentUserProvider.isStaff()
                ? shipmentOrderRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Shipment order with the provided ID: " + id + " doesn't exist."))
                : shipmentOrderRepository.findByIdAndCustomerId(id, currentUser.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Shipment order with the provided ID: " + id + " doesn't exist."));

        return shipmentOrderMapper.toResponse(shipmentOrder, freightRepository.countByShipmentOrderId(id));
    }

    @Transactional
    public ShipmentOrderResponse updateShipmentOrder(UUID id, CreateUpdateShipmentOrder updateShipmentOrder) {

        ShipmentOrder shipmentOrder = shipmentOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment order with the provided ID: " + id + " doesn't exist."));

        if(!currentUserProvider.isOwnerOrStaff(shipmentOrder.getCustomer().getId())){
            throw new ResourceNotFoundException("The shipment order with the provided ID:" + id + " doesn't exist");
        }


        Location startLocation =
                locationRepository.findById(updateShipmentOrder.getStartLocationId())
                        .orElseThrow(() -> new ResourceNotFoundException("Can't find the location with the specified ID: " + updateShipmentOrder.getStartLocationId()));
        Location endLocation = locationRepository.findById(updateShipmentOrder.getEndLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Can't find the location with the specified ID: " + updateShipmentOrder.getEndLocationId()));


        shipmentOrder.setStartLocation(startLocation);
        shipmentOrder.setEndLocation(endLocation);

        ShipmentOrder savedShipmentOrder= shipmentOrderRepository.save(shipmentOrder);

        return shipmentOrderMapper.toResponse(savedShipmentOrder, freightRepository.countByShipmentOrderId(id));

    }


    @Transactional
    public ShipmentOrderResponse patchShipmentOrder(UUID id, PatchShipmentOrder updateShipmentOrder) {

        ShipmentOrder shipmentOrder = shipmentOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment order with the provided ID: " + id + " doesn't exist."));

        if(!currentUserProvider.isOwnerOrStaff(shipmentOrder.getCustomer().getId())){
            throw new ResourceNotFoundException("The shipment order with the provided ID:" + id + " doesn't exist");
        }


        UUID startLocationId = updateShipmentOrder.getStartLocationId();
        UUID endLocationId = updateShipmentOrder.getEndLocationId();

        if (startLocationId != null){
            Location startLocation =
                    locationRepository.findById(startLocationId)
                            .orElseThrow(() -> new ResourceNotFoundException("Can't find the location with the specified ID: " + updateShipmentOrder.getStartLocationId()));
            shipmentOrder.setStartLocation(startLocation);

        }

        if (endLocationId != null){
             Location endLocation = locationRepository.findById(endLocationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Can't find the location with the specified ID: " + updateShipmentOrder.getEndLocationId()));
            shipmentOrder.setEndLocation(endLocation);
        }


        ShipmentOrder savedShipmentOrder= shipmentOrderRepository.save(shipmentOrder);

        return shipmentOrderMapper.toResponse(savedShipmentOrder, freightRepository.countByShipmentOrderId(id));

    }

    @Transactional
    public ShipmentOrderResponse updateShipmentOrderStatus(
            UUID id,
            UpdateShipmentOrderStatus request
    ) {
        ShipmentOrder shipmentOrder = shipmentOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipment order with the provided ID: " + id + " doesn't exist."
                ));

        ShipmentOrderStatus current = shipmentOrder.getStatus();
        ShipmentOrderStatus next = request.getStatus();

        if (current.canTransitionTo(next)){
            shipmentOrder.setStatus(request.getStatus());

            return shipmentOrderMapper.toResponse(
                    shipmentOrder,
                    freightRepository.countByShipmentOrderId(id)
            );
        }else{
            List<String> allowed = current.allowedNext().stream().map(Enum::toString).toList();
            throw new BusinessRuleViolationException("Cannot move order from "+ current + " to " + next, allowed);
        }


    }

    @Transactional
    public void deleteShipmentOrder(UUID id) {


        ShipmentOrder shipmentOrder = shipmentOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipment order with the provided ID: " + id + " doesn't exist."
                ));

        if(!currentUserProvider.isOwnerOrStaff(shipmentOrder.getCustomer().getId())){
            throw new ResourceNotFoundException("The shipment order with the provided ID:" + id + " doesn't exist");
        }

        ShipmentOrderStatus shipmentOrderStatus = shipmentOrder.getStatus();

        if(!shipmentOrderStatus.canTransitionTo(ShipmentOrderStatus.CANCELLED)){
            throw new BusinessRuleViolationException(
                    "Cannot cancel an order in status " + shipmentOrderStatus,
                    shipmentOrderStatus.allowedNext().stream().map(Enum::name).toList()
            );
        }

        shipmentOrder.setStatus(ShipmentOrderStatus.CANCELLED);
        shipmentOrderRepository.save(shipmentOrder);
    }



}
