package com.zavrsniRad.LogisticsApp.domain.dto.crew;

import com.zavrsniRad.LogisticsApp.domain.dto.user.UserResponse;
import lombok.*;

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
