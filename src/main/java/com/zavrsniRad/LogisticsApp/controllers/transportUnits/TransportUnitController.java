package com.zavrsniRad.LogisticsApp.controllers.transportUnits;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.TransportUnitResponse;
import com.zavrsniRad.LogisticsApp.service.TransportUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transport-units")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
public class TransportUnitController {

    private final TransportUnitService transportUnitService;


    @GetMapping
    public ResponseEntity<List<TransportUnitResponse>> getAllTransportUnits(){

        List<TransportUnitResponse> units = transportUnitService.getAllTransportUnits();
        return ResponseEntity.status(HttpStatus.OK).body(units);

    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<TransportUnitResponse> getTransportUnit(
            @PathVariable UUID id
    ){

        TransportUnitResponse unit = transportUnitService.getTransportUnit(id);
        return ResponseEntity.status(HttpStatus.OK).body(unit);

    }

}
