package com.zavrsniRad.LogisticsApp.controllers.crew;

import com.zavrsniRad.LogisticsApp.domain.dto.crew.CreateUpdateCrewMember;
import com.zavrsniRad.LogisticsApp.domain.dto.crew.CrewAssignmentResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.crew.CrewMemberResponse;
import com.zavrsniRad.LogisticsApp.service.CrewAssignmentService;
import com.zavrsniRad.LogisticsApp.service.CrewMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/crew-members")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
public class CrewMemberController {

    private final CrewMemberService crewMemberService;
    private final CrewAssignmentService crewAssignmentService;


    @GetMapping
    public ResponseEntity<List<CrewMemberResponse>> getAllCrewMembers(){

        List<CrewMemberResponse> crewMembers = crewMemberService.getAllCrewMembers();
        return ResponseEntity.status(HttpStatus.OK).body(crewMembers);

    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<CrewMemberResponse> getCrewMember(
            @PathVariable UUID id
    ){

        CrewMemberResponse crewMember = crewMemberService.getCrewMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(crewMember);

    }


    @GetMapping(path = "/{id}/assignments")
    public ResponseEntity<List<CrewAssignmentResponse>> getAssignmentsForCrewMember(
            @PathVariable UUID id
    ){

        List<CrewAssignmentResponse> assignments = crewAssignmentService.getAssignmentsForCrewMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(assignments);

    }


    @PostMapping
    public ResponseEntity<CrewMemberResponse> createCrewMember(
            @Valid @RequestBody CreateUpdateCrewMember createCrewMember
    ){

        CrewMemberResponse crewMember = crewMemberService.createCrewMember(createCrewMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(crewMember);

    }


    @PutMapping(path = "/{id}")
    public ResponseEntity<CrewMemberResponse> updateCrewMember(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUpdateCrewMember updateCrewMember
    ){

        CrewMemberResponse crewMember = crewMemberService.updateCrewMember(id, updateCrewMember);
        return ResponseEntity.status(HttpStatus.OK).body(crewMember);

    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteCrewMember(
            @PathVariable UUID id
    ){

        crewMemberService.deleteCrewMember(id);
        return ResponseEntity.noContent().build();

    }

}
