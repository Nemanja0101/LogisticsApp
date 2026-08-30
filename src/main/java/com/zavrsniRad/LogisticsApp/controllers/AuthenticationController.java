package com.zavrsniRad.LogisticsApp.controllers;


import com.zavrsniRad.LogisticsApp.domain.dto.LoginRequest;
import com.zavrsniRad.LogisticsApp.domain.dto.LoginResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.SignUpRequest;
import com.zavrsniRad.LogisticsApp.domain.dto.SignUpResponse;
import com.zavrsniRad.LogisticsApp.domain.models.RefreshToken;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import com.zavrsniRad.LogisticsApp.security.LogisticsUserDetails;
import com.zavrsniRad.LogisticsApp.service.AuthenticationService;
import com.zavrsniRad.LogisticsApp.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.cookie.secure:false}")
    private boolean secureCookie;

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){

        UserDetails authenticated =
                authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        User user = ((LogisticsUserDetails) authenticated).getUser();

        String accessToken  = authenticationService.generateToken(authenticated);
        String refreshToken = refreshTokenService.generate(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildAccessCookie(accessToken).toString())
                .header(HttpHeaders.SET_COOKIE, buildRefreshCookie(refreshToken).toString())
                .body(LoginResponse
                        .builder()
                        .username(authenticated.getUsername())
                        .success(true)
                        .build());

    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(value="refreshToken", required = false) String UserRefreshToken){

        if (UserRefreshToken == null) {
            throw new BadCredentialsException("Missing refresh token");
        }

        RefreshToken stored = refreshTokenService.validate(UserRefreshToken);
        User user = stored.getUser();
//     if this doesnt throw, then rotate.

        refreshTokenService.revoke(stored);
        String newRefreshToken = refreshTokenService.generate(user);

        UserDetails userDetails = new LogisticsUserDetails(user);
        String newAccessToken = authenticationService.generateToken(userDetails);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, buildAccessCookie(newAccessToken).toString())
                .header(HttpHeaders.SET_COOKIE, buildRefreshCookie(newRefreshToken).toString())
                .body(LoginResponse.builder().username(user.getEmail()).success(true).build());
    }


    @PostMapping(path= "/logout")
    public ResponseEntity<String> logout(
            @CookieValue(value="refreshToken", required = false) String UserRefreshToken){

        if (UserRefreshToken != null) {
            try {
                refreshTokenService.revoke(refreshTokenService.validate(UserRefreshToken));
            } catch (BadCredentialsException ignored) {
                // already invalid/expired . nothing to revoke
            }
        }

        ResponseCookie expiredAccess = ResponseCookie.from("jwt", "")
                .httpOnly(true).secure(secureCookie).path("/").maxAge(0).sameSite("Strict").build();
        ResponseCookie expiredRefresh = ResponseCookie.from("refreshToken", "")
                .httpOnly(true).secure(secureCookie).path("/api/v1/auth").maxAge(0).sameSite("Strict").build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredAccess.toString())
                .header(HttpHeaders.SET_COOKIE, expiredRefresh.toString())
                .build();

    }


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




    private ResponseCookie buildRefreshCookie(String refreshToken) {

        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(secureCookie) //meaning it can go over http too
                .path("/api/v1/auth")
                .maxAge(7 * 24 * 60 * 60) //7 days
                .sameSite("Lax")
                .build();

    }

    private ResponseCookie buildAccessCookie(String accessToken) {

        return ResponseCookie.from("jwt", accessToken)
                .httpOnly(true)
                .secure(secureCookie) //meaning it can go over http too
                .path("/")
                .maxAge(15 * 60) //15 minutes
                .sameSite("Lax")
                .build();

    }



}
