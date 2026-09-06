package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateVessel;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.VesselResponse;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Vessel;
import com.zavrsniRad.LogisticsApp.exceptions.BusinessRuleViolationException;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.VesselMapper;
import com.zavrsniRad.LogisticsApp.repositories.ShipmentSegmentRepository;
import com.zavrsniRad.LogisticsApp.repositories.VesselRepository;
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
public class VesselService {

    private final VesselMapper vesselMapper;
    private final VesselRepository vesselRepository;
    private final ShipmentSegmentRepository shipmentSegmentRepository;

    public Page<VesselResponse> getAllVessels(Pageable pageable) {

        return vesselRepository.findAll(pageable).map(vesselMapper::toResponse);

    }

    public VesselResponse getVessel(UUID id) {

        Vessel vessel = vesselRepository.findWithCapabilitiesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vessel with ID:" + id + " doesn't exist"));

        return vesselMapper.toResponse(vessel);

    }

    public VesselResponse createVessel(CreateUpdateVessel createVessel) {

        if (vesselRepository.existsByImoNumber(createVessel.getImoNumber())) {
            throw new BusinessRuleViolationException("The vessel you're trying to create in the system, already has it's IMO number registered to another vessel.",
                    new ArrayList<>(List.of(createVessel.getImoNumber())));
        }

        Vessel vessel = vesselMapper.toVessel(createVessel);
        Vessel saved = vesselRepository.save(vessel);

        return vesselMapper.toResponse(saved);

    }

    @Transactional
    public VesselResponse updateVessel(UUID id, CreateUpdateVessel updateVessel) {

        Vessel vessel = vesselRepository.findWithCapabilitiesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vessel with ID:" + id + " doesn't exist"));

        if (vesselRepository.existsByImoNumberAndIdNot(updateVessel.getImoNumber(), id)) {
            throw new BusinessRuleViolationException("The IMO number is already registered to another vessel.",
                    new ArrayList<>(List.of(updateVessel.getImoNumber())));
        }

        vessel.setImoNumber(updateVessel.getImoNumber());
        vessel.setPortOfRegistry(updateVessel.getPortOfRegistry());
        vessel.setOperatedBy(updateVessel.getOperatedBy());

        vessel.getCapabilities().clear();

        if (updateVessel.getCapabilities() != null) {
            vessel.getCapabilities().addAll(updateVessel.getCapabilities());
        }

        Vessel saved = vesselRepository.save(vessel);

        return vesselMapper.toResponse(saved);

    }

    @Transactional
    public void deleteVessel(UUID id) {

        Vessel vessel = vesselRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vessel with ID:" + id + " doesn't exist"));

        if (shipmentSegmentRepository.existsByTransportUnitId(vessel.getId())) {
            throw new BusinessRuleViolationException("Vessel is assigned to one or more shipment segments", new ArrayList<>());
        }

        vesselRepository.delete(vessel);

    }
}
