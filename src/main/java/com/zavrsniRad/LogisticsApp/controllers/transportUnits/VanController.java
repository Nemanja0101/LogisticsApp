package com.zavrsniRad.LogisticsApp.controllers.transportUnits;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateVan;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.VanResponse;
import com.zavrsniRad.LogisticsApp.service.VanService;
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
@RequestMapping("/api/v1/vans")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
public class VanController {

    private final VanService vanService;


    @GetMapping
    public ResponseEntity<List<VanResponse>> getAllVans(){

        List<VanResponse> vans = vanService.getAllVans();
        return ResponseEntity.status(HttpStatus.OK).body(vans);

    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<VanResponse> getVan(
            @PathVariable UUID id
    ){

        VanResponse van = vanService.getVan(id);
        return ResponseEntity.status(HttpStatus.OK).body(van);

    }


    @PostMapping
    public ResponseEntity<VanResponse> createVan(
            @Valid @RequestBody CreateUpdateVan createVan
    ){

        VanResponse van = vanService.createVan(createVan);
        return ResponseEntity.status(HttpStatus.CREATED).body(van);

    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<VanResponse> updateVan(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUpdateVan updateVan
    ){

        VanResponse van = vanService.updateVan(id, updateVan);
        return ResponseEntity.status(HttpStatus.OK).body(van);

    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteVan(
            @PathVariable UUID id
    ){

        vanService.deleteVan(id);
        return ResponseEntity.noContent().build();

    }

}
