package com.zavrsniRad.LogisticsApp.controllers;


import com.zavrsniRad.LogisticsApp.domain.dto.LoginRequest;
import com.zavrsniRad.LogisticsApp.domain.dto.LoginResponse;
import com.zavrsniRad.LogisticsApp.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){

        UserDetails authenticated = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        String token = authenticationService.generateToken(authenticated);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false) //meaning it can go over http too
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Strict")
                .build();

        LoginResponse response = LoginResponse.builder()
                .username(authenticated.getUsername())
                .success(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    };




}
