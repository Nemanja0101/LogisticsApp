package com.zavrsniRad.LogisticsApp.service;


import com.zavrsniRad.LogisticsApp.domain.dto.shipment.CreateFreight;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.FreightResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.PatchFreight;
import com.zavrsniRad.LogisticsApp.domain.enums.ShipmentOrderStatus;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Freight;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentOrder;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.FreightMapper;
import com.zavrsniRad.LogisticsApp.repositories.FreightRepository;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentOrderRepository;
import com.zavrsniRad.LogisticsApp.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FreightService {

    private static final Set<ShipmentOrderStatus> FREIGHT_EDITABLE_STATUSES = EnumSet.of(
            ShipmentOrderStatus.PENDING,
            ShipmentOrderStatus.CONFIRMED,
            ShipmentOrderStatus.ON_HOLD
    );

    private final FreightRepository freightRepository;
    private final ShipmentOrderRepository shipmentOrderRepository;
    private final FreightMapper freightMapper;
    private final CurrentUserProvider currentUserProvider;

    public FreightResponse createFreight(CreateFreight request, UUID orderId) {

        ShipmentOrder order = shipmentOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment order with id: " + orderId + " not found"));

        if(!currentUserProvider.isOwnerOrStaff(order.getCustomer().getId())){
            throw new ResourceNotFoundException("The shipment order with the provided ID:" + orderId + " doesn't exist");
        }

        assertFreightEditable(order);

        Freight newFreight = freightMapper.toEntity(request);
        newFreight.setShipmentOrder(order);
        Freight savedFreight = freightRepository.save(newFreight);

        return freightMapper.toResponse(savedFreight);

    }

    public List<FreightResponse> getAllFreightsOnShipmentOrder(UUID orderId) {

        ShipmentOrder shipmentOrder = shipmentOrderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Shipment with the ID:" + orderId + "doesn't exist")
        );

        if(!currentUserProvider.isOwnerOrStaff(shipmentOrder.getCustomer().getId())){
            throw new ResourceNotFoundException("The shipment order with the provided ID:" + orderId + " doesn't exist");
        }

        return freightRepository.findAllByShipmentOrderId(orderId)
                .stream()
                .map(freightMapper::toResponse)
                .toList();


    }

    public FreightResponse getSingleFreightOnShipmentOrder(UUID orderId, UUID freightId) {

        ShipmentOrder shipmentOrder = shipmentOrderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Shipment with the ID:" + orderId + "doesn't exist")
        );

        if(!currentUserProvider.isOwnerOrStaff(shipmentOrder.getCustomer().getId())){
            throw new ResourceNotFoundException("The shipment order with the provided ID:" + orderId + " doesn't exist");
        }

        Optional<Freight> oFreight = freightRepository.findByIdAndShipmentOrderId(freightId, orderId);

        if (oFreight.isPresent()){

            return freightMapper.toResponse(oFreight.get());

        }else{
            throw new ResourceNotFoundException("Freight with id: " + freightId + " was not found");
        }


    }

    @Transactional
    public FreightResponse updateFreightOnShipmentOrder(UUID orderId, UUID freightId, CreateFreight updateFreightRequest) {

        Freight freight = getOwnedFreight(orderId, freightId);

        assertFreightEditable(freight.getShipmentOrder());

        freight.setName(updateFreightRequest.getName());
        freight.setDescription(updateFreightRequest.getDescription());
        freight.setWeightKg(updateFreightRequest.getWeightKg());
        freight.setLengthCm(updateFreightRequest.getLengthCm());
        freight.setWidthCm(updateFreightRequest.getWidthCm());
        freight.setHeightCm(updateFreightRequest.getHeightCm());

        Freight saved = freightRepository.save(freight);

        return freightMapper.toResponse(saved);

    }

    @Transactional
    public FreightResponse patchFreightOnShipmentOrder(UUID orderId, UUID freightId, PatchFreight patchFreightRequest) {

        Freight freight = getOwnedFreight(orderId, freightId);

        assertFreightEditable(freight.getShipmentOrder());

        if (patchFreightRequest.getName() != null){
            freight.setName(patchFreightRequest.getName());
        }

        if (patchFreightRequest.getDescription() != null){
            freight.setDescription(patchFreightRequest.getDescription());
        }

        if (patchFreightRequest.getWeightKg() != null){
            freight.setWeightKg(patchFreightRequest.getWeightKg());
        }

        if (patchFreightRequest.getLengthCm() != null){
            freight.setLengthCm(patchFreightRequest.getLengthCm());
        }

        if (patchFreightRequest.getWidthCm() != null){
            freight.setWidthCm(patchFreightRequest.getWidthCm());
        }

        if (patchFreightRequest.getHeightCm() != null){
            freight.setHeightCm(patchFreightRequest.getHeightCm());
        }

        Freight saved = freightRepository.save(freight);

        return freightMapper.toResponse(saved);

    }

    @Transactional
    public void deleteFreightOnShipmentOrder(UUID orderId, UUID freightId) {

        Freight freight = getOwnedFreight(orderId, freightId);

        assertFreightEditable(freight.getShipmentOrder());

        freightRepository.delete(freight);

    }

    private Freight getOwnedFreight(UUID orderId, UUID freightId) {

        ShipmentOrder shipmentOrder = shipmentOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment order with id: " + orderId + " not found"));


        if(!currentUserProvider.isOwnerOrStaff(shipmentOrder.getCustomer().getId())){
            throw new ResourceNotFoundException("The shipment order with the provided ID:" + orderId + " doesn't exist");
        }

        return freightRepository.findByIdAndShipmentOrderId(freightId, orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Freight with id: " + freightId + " was not found"));

    }

    private void assertFreightEditable(ShipmentOrder shipmentOrder){

        ShipmentOrderStatus status = shipmentOrder.getStatus();

        if (!FREIGHT_EDITABLE_STATUSES.contains(status)){
            throw new BusinessRuleViolationException(
                    "Freight can't be changed while the order is in status " + status,
                    FREIGHT_EDITABLE_STATUSES.stream().map(Enum::name).toList()
            );
        }

    }
}
