package com.zavrsniRad.LogisticsApp.controllers;


import com.zavrsniRad.LogisticsApp.domain.dto.location.CreateUpdateLocation;
import com.zavrsniRad.LogisticsApp.domain.dto.location.PatchLocation;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.LocationResponse;
import com.zavrsniRad.LogisticsApp.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<Page<LocationResponse>> getAllLocations(
            @PageableDefault(sort = {"city", "id"}, direction = Sort.Direction.ASC) Pageable pageable
    ){

        return ResponseEntity.status(HttpStatus.OK).body(locationService.getAllLocations(pageable));

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<LocationResponse> getLocation(@PathVariable UUID id) {


        LocationResponse locationResponse = locationService.getLocation(id);

        return ResponseEntity.status(HttpStatus.OK).body(locationResponse);

    }

    @PostMapping
    public ResponseEntity<?> createLocation(
            @Valid @RequestBody CreateUpdateLocation createUpdateLocation
    ){

        LocationResponse createdLocation = locationService.createLocation(createUpdateLocation);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
    }


    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> updateLocation(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUpdateLocation updateLocation
    ) {

        LocationResponse updatedLocation =
                locationService.updateLocation(id, updateLocation);

        return ResponseEntity.status(HttpStatus.OK).body(updatedLocation);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<LocationResponse> patchLocation(
            @PathVariable UUID id,
            @RequestBody PatchLocation updateLocation
    ) {

        LocationResponse updatedLocation =
                locationService.patchLocation(id, updateLocation);

        return ResponseEntity.status(HttpStatus.OK).body(updatedLocation);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable UUID id
    ) {

        locationService.deleteLocation(id);

        return ResponseEntity.noContent().build();
    }
}