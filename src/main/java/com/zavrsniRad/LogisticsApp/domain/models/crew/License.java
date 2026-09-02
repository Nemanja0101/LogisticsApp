package com.zavrsniRad.LogisticsApp.domain.models.crew;

import com.zavrsniRad.LogisticsApp.domain.enums.LicenseType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class License {

    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    @Column(name = "license_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LicenseType licenseType; // e.g. "CDL-A", "Maritime Officer", etc.

    @Column(name = "issued_at", nullable = false)
    private LocalDate issuedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDate expiresAt;

    @Column(name = "adr_certified", nullable = false)
    @Builder.Default
    private boolean adrCertified = false;
}
