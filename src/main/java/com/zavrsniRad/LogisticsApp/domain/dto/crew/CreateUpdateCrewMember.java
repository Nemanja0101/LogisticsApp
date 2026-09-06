package com.zavrsniRad.LogisticsApp.domain.dto.crew;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUpdateCrewMember {

    @NotNull
    private UUID userId;

    @NotNull
    @Valid
    private LicenseDto license;

}
