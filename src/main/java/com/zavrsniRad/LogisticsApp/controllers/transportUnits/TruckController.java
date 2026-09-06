package com.zavrsniRad.LogisticsApp.controllers.transportUnits;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateTruck;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.TruckResponse;
import com.zavrsniRad.LogisticsApp.service.TruckService;
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
@RequestMapping("/api/v1/trucks")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
public class TruckController {

    private final TruckService truckService;


    @GetMapping
    public ResponseEntity<Page<TruckResponse>> getAllTrucks(
            @PageableDefault(sort = {"createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable
    ){

        return ResponseEntity.status(HttpStatus.OK).body(truckService.getAllTrucks(pageable));

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
