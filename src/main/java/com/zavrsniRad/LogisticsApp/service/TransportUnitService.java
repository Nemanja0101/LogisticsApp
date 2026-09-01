package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.TransportUnitResponse;
import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.TransportUnit;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.TransportUnitMapper;
import com.zavrsniRad.LogisticsApp.repositories.TransportUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransportUnitService {

    private final TransportUnitRepository transportUnitRepository;
    private final TransportUnitMapper transportUnitMapper;

    public List<TransportUnitResponse> getAllTransportUnits() {


        List<TransportUnit> allTransportUnits = transportUnitRepository.findAll();

        return allTransportUnits.stream().map(transportUnitMapper::toResponse).toList();


    }

    public TransportUnitResponse getTransportUnit(UUID id) {

        TransportUnit transportUnit = transportUnitRepository.findWithCapabilitiesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transport unit with ID:" + id + "doesn't exist"));

        return transportUnitMapper.toResponse(transportUnit);

    }
}
