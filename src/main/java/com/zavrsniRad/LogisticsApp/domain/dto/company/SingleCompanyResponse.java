package com.zavrsniRad.LogisticsApp.domain.dto.company;


import com.zavrsniRad.LogisticsApp.domain.dto.company.member.CompanyMemberResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleCompanyResponse {

    private UUID id;
    private String name;
    private String registrationNumber;
    private LocalDateTime createdAt;
    private List<CompanyMemberResponse> members;
}
