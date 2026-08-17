package com.zavrsniRad.LogisticsApp.domain.dto;


import com.zavrsniRad.LogisticsApp.annotations.PasswordMatches;
import com.zavrsniRad.LogisticsApp.annotations.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatches
public class SignUpRequest {

    @NotBlank(message = "You must provide an email")
    @Email(message = "The email address must be of valid format")
    private String email;

    @NotBlank(message = "You must provide your first name")
    private String firstName;

    @NotBlank(message = "You must provide your last name")
    private String lastName;

    @ValidPassword
    private String password;
    private String confirmPassword;

}
