package com.zavrsniRad.LogisticsApp.controllers.transportUnits;


import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.AircraftResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateAircraft;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateRailcar;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.RailcarResponse;
import com.zavrsniRad.LogisticsApp.service.AircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/aircraft")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
public class AircraftController {

    private final AircraftService aircraftService;


    @GetMapping
    public ResponseEntity<Page<AircraftResponse>> getAllAircraft(
            @PageableDefault(sort = {"createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable
    ){

        return ResponseEntity.status(HttpStatus.OK).body(aircraftService.getAllAircraft(pageable));

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AircraftResponse> getAircraft(
            @PathVariable UUID id
    ){

        AircraftResponse aircraft = aircraftService.getAircraft(id);
        return ResponseEntity.status(HttpStatus.OK).body(aircraft);

    }


    @PostMapping ResponseEntity<AircraftResponse> createAircraft(
            @Valid @RequestBody CreateUpdateAircraft createAircraft
    ){
        AircraftResponse aircraftResponse = aircraftService.createAircraft(createAircraft);
        return ResponseEntity.status(HttpStatus.CREATED).body(aircraftResponse);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<AircraftResponse> updateAircraft(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUpdateAircraft updateAircraft
    ){

        AircraftResponse aircraft = aircraftService.updateAircraft(id, updateAircraft);
        return ResponseEntity.status(HttpStatus.OK).body(aircraft);

    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteAircraft(
            @PathVariable UUID id
    ){

        aircraftService.deleteAircraft(id);
        return ResponseEntity.noContent().build();

    }

}
