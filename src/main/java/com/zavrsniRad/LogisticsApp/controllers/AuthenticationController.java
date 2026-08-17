package com.zavrsniRad.LogisticsApp.controllers;


import com.zavrsniRad.LogisticsApp.domain.dto.LoginRequest;
import com.zavrsniRad.LogisticsApp.domain.dto.LoginResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.SignUpRequest;
import com.zavrsniRad.LogisticsApp.domain.dto.SignUpResponse;
import com.zavrsniRad.LogisticsApp.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){

        UserDetails authenticated = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        String token = authenticationService.generateToken(authenticated);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false) //meaning it can go over http too
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();

        LoginResponse response = LoginResponse.builder()
                .username(authenticated.getUsername())
                .success(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    };

    @PostMapping(path = "/signup")
    public ResponseEntity<SignUpResponse> signUp(
            @Valid @RequestBody SignUpRequest signUpRequest){

        SignUpResponse signUpResponse =authenticationService.signUp(signUpRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("api/v1/users/{id}")
                .buildAndExpand(signUpResponse.getId())
                .toUri();

        return ResponseEntity.created(location).body(signUpResponse);

    }

    @PostMapping(path= "/logout")
    public ResponseEntity<String> logout(HttpServletResponse response){

        ResponseCookie cookie = ResponseCookie.from("jwt", null)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return  ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");

    }




}
