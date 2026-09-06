package com.zavrsniRad.LogisticsApp.controllers.transportUnits;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.TransportUnitResponse;
import com.zavrsniRad.LogisticsApp.service.TransportUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transport-units")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
public class TransportUnitController {

    private final TransportUnitService transportUnitService;


    @GetMapping
    public ResponseEntity<Page<TransportUnitResponse>> getAllTransportUnits(
            @PageableDefault(sort = {"createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable
    ){

        return ResponseEntity.status(HttpStatus.OK).body(transportUnitService.getAllTransportUnits(pageable));

    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<TransportUnitResponse> getTransportUnit(
            @PathVariable UUID id
    ){

        TransportUnitResponse unit = transportUnitService.getTransportUnit(id);
        return ResponseEntity.status(HttpStatus.OK).body(unit);

    }

}
