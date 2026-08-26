package com.zavrsniRad.LogisticsApp.domain.dto.company;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchCompanyRequest {

    private String name;
    private String registrationNumber;

}
