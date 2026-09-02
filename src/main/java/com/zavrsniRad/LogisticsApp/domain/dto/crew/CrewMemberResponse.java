package com.zavrsniRad.LogisticsApp.domain.dto.crew;

import com.zavrsniRad.LogisticsApp.domain.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrewMemberResponse {

    private UUID id;
    private UserResponse user;
    private LicenseDto license;
    private LocalDateTime createdAt;

}
