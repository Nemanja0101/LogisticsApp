package com.zavrsniRad.LogisticsApp.controllers.transportUnits;

import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.CreateUpdateVessel;
import com.zavrsniRad.LogisticsApp.domain.dto.transportUnits.VesselResponse;
import com.zavrsniRad.LogisticsApp.service.VesselService;
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
@RequestMapping("/api/v1/vessels")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
public class VesselController {

    private final VesselService vesselService;


    @GetMapping
    public ResponseEntity<List<VesselResponse>> getAllVessels(){

        List<VesselResponse> vessels = vesselService.getAllVessels();
        return ResponseEntity.status(HttpStatus.OK).body(vessels);

    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<VesselResponse> getVessel(
            @PathVariable UUID id
    ){

        VesselResponse vessel = vesselService.getVessel(id);
        return ResponseEntity.status(HttpStatus.OK).body(vessel);

    }


    @PostMapping
    public ResponseEntity<VesselResponse> createVessel(
            @Valid @RequestBody CreateUpdateVessel createVessel
    ){

        VesselResponse vessel = vesselService.createVessel(createVessel);
        return ResponseEntity.status(HttpStatus.CREATED).body(vessel);

    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<VesselResponse> updateVessel(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUpdateVessel updateVessel
    ){

        VesselResponse vessel = vesselService.updateVessel(id, updateVessel);
        return ResponseEntity.status(HttpStatus.OK).body(vessel);

    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteVessel(
            @PathVariable UUID id
    ){

        vesselService.deleteVessel(id);
        return ResponseEntity.noContent().build();

    }

}
