package com.zavrsniRad.LogisticsApp.domain.dto.user;

import com.zavrsniRad.LogisticsApp.domain.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRole {

    @NotNull
    private UserRole userRole;

}
