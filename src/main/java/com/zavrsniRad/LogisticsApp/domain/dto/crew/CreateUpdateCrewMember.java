package com.zavrsniRad.LogisticsApp.domain.dto.crew;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
