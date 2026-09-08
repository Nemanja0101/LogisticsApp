package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateRailcar;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.RailcarResponse;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Railcar;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.RailcarMapper;
import com.zavrsniRad.LogisticsApp.repositories.RailcarRepository;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentSegmentRepository;
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
public class RailcarService {

    private final RailcarMapper railcarMapper;
    private final RailcarRepository railcarRepository;
    private final ShipmentSegmentRepository shipmentSegmentRepository;
    private final TransportCompatibilityChecker transportCompatibilityChecker;

    public Page<RailcarResponse> getAllRailcars(Pageable pageable) {

        return railcarRepository.findAll(pageable).map(railcarMapper::toResponse);

    }

    public RailcarResponse getRailcar(UUID id) {

        Railcar railcar = railcarRepository.findWithCapabilitiesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Railcar with ID:" + id + " doesn't exist"));

        return railcarMapper.toResponse(railcar);

    }

    public RailcarResponse createRailcar(CreateUpdateRailcar createRailcar) {

        if (railcarRepository.existsByWagonNumber(createRailcar.getWagonNumber())) {
            throw new BusinessRuleViolationException("The railcar you're trying to create in the system, already has it's wagon number registered to another railcar.",
                    new ArrayList<>(List.of(createRailcar.getWagonNumber())));
        }

        transportCompatibilityChecker.assertCapabilitiesValidForUnitType(TransportUnitType.RAILCAR, createRailcar.getCapabilities());

        Railcar railcar = railcarMapper.toRailcar(createRailcar);
        Railcar saved = railcarRepository.save(railcar);

        return railcarMapper.toResponse(saved);

    }

    @Transactional
    public RailcarResponse updateRailcar(UUID id, CreateUpdateRailcar updateRailcar) {

        Railcar railcar = railcarRepository.findWithCapabilitiesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Railcar with ID:" + id + " doesn't exist"));

        if (railcarRepository.existsByWagonNumberAndIdNot(updateRailcar.getWagonNumber(), id)) {
            throw new BusinessRuleViolationException("The wagon number is already registered to another railcar.",
                    new ArrayList<>(List.of(updateRailcar.getWagonNumber())));
        }

        railcar.setWagonNumber(updateRailcar.getWagonNumber());
        railcar.setOperatedBy(updateRailcar.getOperatedBy());
        railcar.setMaxWeightKg(updateRailcar.getMaxWeightKg());
        railcar.setCargoVolumeM3(updateRailcar.getCargoVolumeM3());

        transportCompatibilityChecker.assertCapabilitiesValidForUnitType(TransportUnitType.RAILCAR, updateRailcar.getCapabilities());

        railcar.getCapabilities().clear();

        if (updateRailcar.getCapabilities() != null) {
            railcar.getCapabilities().addAll(updateRailcar.getCapabilities());
        }

        Railcar saved = railcarRepository.save(railcar);

        return railcarMapper.toResponse(saved);

    }

    @Transactional
    public void deleteRailcar(UUID id) {

        Railcar railcar = railcarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Railcar with ID:" + id + " doesn't exist"));

        if (shipmentSegmentRepository.existsByTransportUnitId(railcar.getId())) {
            throw new BusinessRuleViolationException("Railcar is assigned to one or more shipment segments", new ArrayList<>());
        }

        railcarRepository.delete(railcar);

    }
}
