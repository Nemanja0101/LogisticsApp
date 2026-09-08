package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.AircraftResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateAircraft;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Aircraft;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.AircraftMapper;
import com.zavrsniRad.LogisticsApp.repositories.AircraftRepository;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentSegmentRepository;
import jakarta.validation.Valid;
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
public class AircraftService {

    private final AircraftMapper aircraftMapper;
    private final AircraftRepository aircraftRepository;
    private final ShipmentSegmentRepository shipmentSegmentRepository;
    private final TransportCompatibilityChecker transportCompatibilityChecker;

    public Page<AircraftResponse> getAllAircraft(Pageable pageable) {

        return aircraftRepository.findAll(pageable).map(aircraftMapper::toResponse);

    }

    public AircraftResponse getAircraft(UUID id) {

        Aircraft aircraft = aircraftRepository.findWithCapabilitiesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft with ID:" + id + " doesn't exist"));

        return aircraftMapper.toResponse(aircraft);
    }

    public AircraftResponse createAircraft(CreateUpdateAircraft createAircraft) {

        if (aircraftRepository.existsByRegistrationNumber(createAircraft.getRegistrationNumber())){
            throw new BusinessRuleViolationException("The aircraft you're trying to create in the system, already has it's registration number registered to another aircraft.",
                    new ArrayList<>(List.of(createAircraft.getRegistrationNumber())));
        }

        transportCompatibilityChecker.assertCapabilitiesValidForUnitType(TransportUnitType.AIRCRAFT, createAircraft.getCapabilities());

        Aircraft aircraft = aircraftMapper.toEntity(createAircraft);
        Aircraft saved = aircraftRepository.save(aircraft);

        return aircraftMapper.toResponse(saved);
    }

    @Transactional
    public AircraftResponse updateAircraft(UUID id, @Valid CreateUpdateAircraft updateAircraft) {

        Aircraft aircraft = aircraftRepository.findWithCapabilitiesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft with ID:" + id + " doesn't exist"));

        if (aircraftRepository.existsByRegistrationNumberAndIdNot(updateAircraft.getRegistrationNumber(), id)){
            throw new BusinessRuleViolationException("The aircraft you're trying to change in the system, already has it's registration number registered to another aircraft.",
                    new ArrayList<>(List.of(updateAircraft.getRegistrationNumber())));
        }

        aircraft.setRegistrationNumber(updateAircraft.getRegistrationNumber());
        aircraft.setManufacturer(updateAircraft.getManufacturer());
        aircraft.setModel(updateAircraft.getModel());
        aircraft.setMaxWeightKg(updateAircraft.getMaxWeightKg());
        aircraft.setCargoVolumeM3(updateAircraft.getCargoVolumeM3());
        aircraft.setMaxRangeKm(updateAircraft.getMaxRangeKm());
        aircraft.setOperatedBy(updateAircraft.getOperatedBy());

        transportCompatibilityChecker.assertCapabilitiesValidForUnitType(TransportUnitType.AIRCRAFT, updateAircraft.getCapabilities());

        aircraft.getCapabilities().clear();

        if (updateAircraft.getCapabilities() != null) {
            aircraft.getCapabilities().addAll(updateAircraft.getCapabilities());
        }

        Aircraft saved = aircraftRepository.save(aircraft);

        return aircraftMapper.toResponse(saved);

    }

    @Transactional
    public void deleteAircraft(UUID id) {

        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft with ID:" + id + " doesn't exist"));

        if (shipmentSegmentRepository.existsByTransportUnitId(aircraft.getId())) {
            throw new BusinessRuleViolationException("Aircraft is assigned to one or more shipment segments", new ArrayList<>());
        }

        aircraftRepository.delete(aircraft);

    }

}
