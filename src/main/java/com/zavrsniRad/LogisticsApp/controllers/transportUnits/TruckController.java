package com.zavrsniRad.LogisticsApp.controllers.transportUnits;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateTruck;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.TruckResponse;
import com.zavrsniRad.LogisticsApp.service.TruckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trucks")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
public class TruckController {

    private final TruckService truckService;


    @GetMapping
    public ResponseEntity<List<TruckResponse>> getAllTrucks(){

        List<TruckResponse> trucks = truckService.getAllTrucks();
        return ResponseEntity.status(HttpStatus.OK).body(trucks);

    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<TruckResponse> getTruck(
            @PathVariable UUID id
    ){

        TruckResponse truck = truckService.getTruck(id);
        return ResponseEntity.status(HttpStatus.OK).body(truck);

    }


    @PostMapping
    public ResponseEntity<TruckResponse> createTruck(
            @Valid @RequestBody CreateUpdateTruck createTruck
    ){

        TruckResponse truck = truckService.createTruck(createTruck);
        return ResponseEntity.status(HttpStatus.CREATED).body(truck);

    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<TruckResponse> updateTruck(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUpdateTruck updateTruck
    ){

        TruckResponse truck = truckService.updateTruck(id, updateTruck);
        return ResponseEntity.status(HttpStatus.OK).body(truck);

    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTruck(
            @PathVariable UUID id
    ){

        truckService.deleteTruck(id);
        return ResponseEntity.noContent().build();

    }

}
