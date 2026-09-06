package com.zavrsniRad.LogisticsApp.controllers.crew;

import com.zavrsniRad.LogisticsApp.domain.dto.crew.CreateUpdateCrewMember;
import com.zavrsniRad.LogisticsApp.domain.dto.crew.CrewAssignmentResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.crew.CrewMemberResponse;
import com.zavrsniRad.LogisticsApp.service.CrewAssignmentService;
import com.zavrsniRad.LogisticsApp.service.CrewMemberService;
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
    public ResponseEntity<Page<CrewMemberResponse>> getAllCrewMembers(
            @PageableDefault(sort = {"createdAt", "id"}, direction = Sort.Direction.DESC) Pageable pageable
    ){

        return ResponseEntity.status(HttpStatus.OK).body(crewMemberService.getAllCrewMembers(pageable));

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
