package com.zavrsniRad.LogisticsApp.controllers.transportUnits;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateRailcar;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.RailcarResponse;
import com.zavrsniRad.LogisticsApp.service.RailcarService;
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
@RequestMapping("/api/v1/railcars")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
public class RailcarController {

    private final RailcarService railcarService;


    @GetMapping
    public ResponseEntity<Page<RailcarResponse>> getAllRailcars(
            @PageableDefault(sort = {"createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable
    ){

        return ResponseEntity.status(HttpStatus.OK).body(railcarService.getAllRailcars(pageable));

    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<RailcarResponse> getRailcar(
            @PathVariable UUID id
    ){

        RailcarResponse railcar = railcarService.getRailcar(id);
        return ResponseEntity.status(HttpStatus.OK).body(railcar);

    }


    @PostMapping
    public ResponseEntity<RailcarResponse> createRailcar(
            @Valid @RequestBody CreateUpdateRailcar createRailcar
    ){

        RailcarResponse railcar = railcarService.createRailcar(createRailcar);
        return ResponseEntity.status(HttpStatus.CREATED).body(railcar);

    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<RailcarResponse> updateRailcar(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUpdateRailcar updateRailcar
    ){

        RailcarResponse railcar = railcarService.updateRailcar(id, updateRailcar);
        return ResponseEntity.status(HttpStatus.OK).body(railcar);

    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteRailcar(
            @PathVariable UUID id
    ){

        railcarService.deleteRailcar(id);
        return ResponseEntity.noContent().build();

    }

}
