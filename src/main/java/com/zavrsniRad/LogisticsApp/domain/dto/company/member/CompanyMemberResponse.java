package com.zavrsniRad.LogisticsApp.domain.dto.company.member;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyMemberResponse {

    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
}
