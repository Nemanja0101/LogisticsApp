package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.TransportUnitResponse;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.TransportUnit;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.TransportUnitMapper;
import com.zavrsniRad.LogisticsApp.repositories.TransportUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransportUnitService {

    private final TransportUnitRepository transportUnitRepository;
    private final TransportUnitMapper transportUnitMapper;

    public Page<TransportUnitResponse> getAllTransportUnits(Pageable pageable) {

        return transportUnitRepository.findAll(pageable).map(transportUnitMapper::toResponse);

    }

    public TransportUnitResponse getTransportUnit(UUID id) {

        TransportUnit transportUnit = transportUnitRepository.findWithCapabilitiesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transport unit with ID:" + id + "doesn't exist"));

        return transportUnitMapper.toResponse(transportUnit);

    }
}
