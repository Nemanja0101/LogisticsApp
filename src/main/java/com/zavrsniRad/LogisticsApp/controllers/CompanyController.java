package com.zavrsniRad.LogisticsApp.controllers;

import com.zavrsniRad.LogisticsApp.domain.dto.company.CompanyResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.company.CreateUpdateCompany;
import com.zavrsniRad.LogisticsApp.domain.dto.company.PatchCompanyRequest;
import com.zavrsniRad.LogisticsApp.domain.dto.company.SingleCompanyResponse;
import com.zavrsniRad.LogisticsApp.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    public final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<CompanyResponse> allCompanies = companyService.getAllCompanies();
        return ResponseEntity.status(HttpStatus.OK).body(allCompanies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleCompanyResponse> getCompany(@PathVariable UUID id) {
        SingleCompanyResponse company = companyService.getCompany(id);
        return ResponseEntity.status(HttpStatus.OK).body(company);
    }


    @PostMapping
    public ResponseEntity<SingleCompanyResponse> createCompany(@Valid @RequestBody CreateUpdateCompany createCompany) {
        SingleCompanyResponse company = companyService.createCompany(createCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SingleCompanyResponse> updateCompany(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUpdateCompany updateRequest
    ) {
        SingleCompanyResponse response = companyService.updateCompany(updateRequest, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SingleCompanyResponse> patchCompany(
            @PathVariable UUID id,
            @RequestBody PatchCompanyRequest patchRequest
    ) {
        SingleCompanyResponse response = companyService.patchCompany(patchRequest, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

}
