package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateVan;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.VanResponse;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Van;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.VanMapper;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentSegmentRepository;
import com.zavrsniRad.LogisticsApp.repositories.VanRepository;
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
public class VanService {

    private final VanMapper vanMapper;
    private final VanRepository vanRepository;
    private final ShipmentSegmentRepository shipmentSegmentRepository;

    public Page<VanResponse> getAllVans(Pageable pageable) {

        return vanRepository.findAll(pageable).map(vanMapper::toResponse);

    }

    public VanResponse getVan(UUID id) {

        Van van = vanRepository.findWithCapabilitiesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Van with ID:" + id + " doesn't exist"));

        return vanMapper.toResponse(van);

    }

    public VanResponse createVan(CreateUpdateVan createVan) {

        if (vanRepository.existsByLicensePlate(createVan.getLicensePlate())) {
            throw new BusinessRuleViolationException("The van you're trying to create in the system, already has it's license plate registered to another van.",
                    new ArrayList<>(List.of(createVan.getLicensePlate())));
        }

        Van van = vanMapper.toVan(createVan);
        Van saved = vanRepository.save(van);

        return vanMapper.toResponse(saved);

    }

    @Transactional
    public VanResponse updateVan(UUID id, CreateUpdateVan updateVan) {

        Van van = vanRepository.findWithCapabilitiesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Van with ID:" + id + " doesn't exist"));

        if (vanRepository.existsByLicensePlateAndIdNot(updateVan.getLicensePlate(), id)) {
            throw new BusinessRuleViolationException("The license plate is already registered to another van.",
                    new ArrayList<>(List.of(updateVan.getLicensePlate())));
        }

        van.setLicensePlate(updateVan.getLicensePlate());
        van.setMaxWeightKg(updateVan.getMaxWeightKg());
        van.setCargoVolumeM3(updateVan.getCargoVolumeM3());
        van.setManufacturer(updateVan.getManufacturer());
        van.setModel(updateVan.getModel());

        van.getCapabilities().clear();

        if (updateVan.getCapabilities() != null) {
            van.getCapabilities().addAll(updateVan.getCapabilities());
        }

        Van saved = vanRepository.save(van);

        return vanMapper.toResponse(saved);

    }

    @Transactional
    public void deleteVan(UUID id) {

        Van van = vanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Van with ID:" + id + " doesn't exist"));

        if (shipmentSegmentRepository.existsByTransportUnitId(van.getId())) {
            throw new BusinessRuleViolationException("Van is assigned to one or more shipment segments", new ArrayList<>());
        }

        vanRepository.delete(van);

    }
}
