package com.zavrsniRad.LogisticsApp.domain.dto.user;

import com.zavrsniRad.LogisticsApp.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUser {

    @NotBlank(message = "You must provide an email")
    @Email(message = "The email address must be of valid format")
    private String email;

    @NotNull
    private UserRole userRole;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

}
