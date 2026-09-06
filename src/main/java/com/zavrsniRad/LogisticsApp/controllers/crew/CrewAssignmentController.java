package com.zavrsniRad.LogisticsApp.controllers.crew;

import com.zavrsniRad.LogisticsApp.domain.dto.crew.CreateCrewAssignment;
import com.zavrsniRad.LogisticsApp.domain.dto.crew.CrewAssignmentResponse;
import com.zavrsniRad.LogisticsApp.service.CrewAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/crew-assignments")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
public class CrewAssignmentController {

    private final CrewAssignmentService crewAssignmentService;


    @GetMapping(path = "/{id}")
    public ResponseEntity<CrewAssignmentResponse> getCrewAssignment(
            @PathVariable UUID id
    ){

        CrewAssignmentResponse assignment = crewAssignmentService.getCrewAssignment(id);
        return ResponseEntity.status(HttpStatus.OK).body(assignment);

    }


    @PostMapping
    public ResponseEntity<CrewAssignmentResponse> createCrewAssignment(
            @Valid @RequestBody CreateCrewAssignment createCrewAssignment
    ){

        CrewAssignmentResponse assignment = crewAssignmentService.createCrewAssignment(createCrewAssignment);
        return ResponseEntity.status(HttpStatus.CREATED).body(assignment);

    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCrewAssignment(
            @PathVariable UUID id
    ){

        crewAssignmentService.deleteCrewAssignment(id);
        return ResponseEntity.noContent().build();

    }

}
