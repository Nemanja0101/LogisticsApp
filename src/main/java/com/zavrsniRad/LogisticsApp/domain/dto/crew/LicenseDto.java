package com.zavrsniRad.LogisticsApp.domain.dto.crew;

import com.zavrsniRad.LogisticsApp.domain.enums.LicenseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private Boolean dangerousGoodsCertified = false;

}
