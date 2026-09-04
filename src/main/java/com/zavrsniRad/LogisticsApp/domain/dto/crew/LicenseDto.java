package com.zavrsniRad.LogisticsApp.domain.dto.crew;

import com.zavrsniRad.LogisticsApp.domain.enums.LicenseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LicenseDto {

    @NotBlank
    private String licenseNumber;

    @NotNull
    private LicenseType licenseType;

    @NotNull
    private LocalDate issuedAt;

    @NotNull
    private LocalDate expiresAt;

    @Builder.Default
    private Boolean adrCertified = false;

}
