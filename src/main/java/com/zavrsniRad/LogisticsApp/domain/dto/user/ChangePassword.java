package com.zavrsniRad.LogisticsApp.domain.dto.user;


import com.zavrsniRad.LogisticsApp.annotations.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {

    @NotBlank
    private String currentPassword;

    @ValidPassword
    private String newPassword;

}
