package com.zavrsniRad.LogisticsApp.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserRequest {

    private String email;
    private String firstName;
    private String lastName;

}
