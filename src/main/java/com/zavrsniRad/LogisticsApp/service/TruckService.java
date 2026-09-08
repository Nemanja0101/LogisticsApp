package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateTruck;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.TruckResponse;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Truck;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.TruckMapper;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentSegmentRepository;
import com.zavrsniRad.LogisticsApp.repositories.TruckRepository;
import com.zavrsniRad.LogisticsApp.domain.enums.TransportUnitType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TruckService {

    private final TruckMapper truckMapper;
    private final TruckRepository truckRepository;
    private final ShipmentSegmentRepository shipmentSegmentRepository;
    private final TransportCompatibilityChecker transportCompatibilityChecker;

    public Page<TruckResponse> getAllTrucks(Pageable pageable) {

        return truckRepository.findAll(pageable).map(truckMapper::toResponse);

    }

    public TruckResponse getTruck(UUID id) {

        Truck truck = truckRepository.findWithCapabilitiesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Truck with ID:" + id + "doesn't exist"));

        return truckMapper.toResponse(truck);

    }

    public TruckResponse createTruck(CreateUpdateTruck createTruck) {

        if(truckRepository.existsByLicensePlate(createTruck.getLicensePlate())){
            throw new BusinessRuleViolationException("The truck you're trying to create in the system, already has it's license plate registered to another truck.",
                    new ArrayList<>(List.of(createTruck.getLicensePlate())));
        }
        transportCompatibilityChecker.assertCapabilitiesValidForUnitType(TransportUnitType.TRUCK, createTruck.getCapabilities());

        Truck truck = truckMapper.toEntity(createTruck);
        Truck saved = truckRepository.save(truck);
        return truckMapper.toResponse(saved);

    }

    @Transactional
    public TruckResponse updateTruck(UUID id, CreateUpdateTruck updateTruck) {

        if (truckRepository.existsByLicensePlateAndIdNot(updateTruck.getLicensePlate(), id)){
            throw new BusinessRuleViolationException("Truck with the license plate: " + updateTruck.getLicensePlate() + " already exists in the system", new ArrayList<>());
        }

        Truck truck = truckRepository.findWithCapabilitiesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Truck with ID:" + id + "doesn't exist"));

        truck.setTrailerType(updateTruck.getTrailerType());
        truck.setAxleCount(updateTruck.getAxleCount());
        truck.setLicensePlate(updateTruck.getLicensePlate());
        truck.setMaxWeightKg(updateTruck.getMaxWeightKg());
        truck.setCargoVolumeM3(updateTruck.getCargoVolumeM3());
        truck.setManufacturer(updateTruck.getManufacturer());
        truck.setModel(updateTruck.getModel());

        transportCompatibilityChecker.assertCapabilitiesValidForUnitType(TransportUnitType.TRUCK, updateTruck.getCapabilities());

        truck.getCapabilities().clear();

        if (updateTruck.getCapabilities() != null) {
            truck.getCapabilities().addAll(updateTruck.getCapabilities());
        }

        Truck saved = truckRepository.save(truck);

        return truckMapper.toResponse(saved);

    }

    @Transactional
    public void deleteTruck(UUID id) {

        Truck truck = truckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Truck with ID:" + id + "doesn't exist"));

        if (shipmentSegmentRepository.existsByTransportUnitId(truck.getId())){
            throw new BusinessRuleViolationException("Truck is assigned to one or more shipment segments", new ArrayList<>());
        }

        truckRepository.delete(truck);

    }
}
