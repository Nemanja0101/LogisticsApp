package com.zavrsniRad.LogisticsApp.controllers;

import com.zavrsniRad.LogisticsApp.domain.dto.company.member.AddCompanyMember;
import com.zavrsniRad.LogisticsApp.domain.dto.company.member.CompanyMemberResponse;
import com.zavrsniRad.LogisticsApp.service.CompanyMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/members")
@RequiredArgsConstructor
public class CompanyMemberController {

    private final CompanyMemberService companyMemberService;


    @GetMapping
    public ResponseEntity<List<CompanyMemberResponse>> getCompanyMembers(
            @PathVariable UUID companyId
    ){

        List<CompanyMemberResponse> members = companyMemberService.getCompanyMembers(companyId);
        return ResponseEntity.status(HttpStatus.OK).body(members);

    }


    @PostMapping
    public ResponseEntity<CompanyMemberResponse> addCompanyMember(
            @PathVariable UUID companyId,
            @Valid @RequestBody AddCompanyMember addCompanyMember
    ){

        CompanyMemberResponse member = companyMemberService.addCompanyMember(companyId, addCompanyMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);

    }


    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> removeCompanyMember(
            @PathVariable UUID companyId,
            @PathVariable UUID userId
    ){

        companyMemberService.removeCompanyMember(companyId, userId);
        return ResponseEntity.noContent().build();

    }

}
