package com.zavrsniRad.LogisticsApp.service;


import com.zavrsniRad.LogisticsApp.domain.enums.UserRole;
import com.zavrsniRad.LogisticsApp.domain.dto.SignUpRequest;
import com.zavrsniRad.LogisticsApp.domain.dto.SignUpResponse;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import com.zavrsniRad.LogisticsApp.exceptions.UserAlreadyExistsException;
import com.zavrsniRad.LogisticsApp.repositories.UserRepository;
import com.zavrsniRad.LogisticsApp.security.LogisticsUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    public UserDetails authenticate(String email, String password){

        User user = userRepository.findByEmailAndDeactivatedAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("Invalid credentials");
        }

        return new LogisticsUserDetails(user);
    }

    private SecretKey getSigningKey() {

        byte[] decoded = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decoded);

    }

    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }


    private Claims extractAllClaims(String token){

//      parser throws an unchecked ExpiredJwtException from parseSignedClaims
//      so this can also be used to validate for expiry
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        try{
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername());
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException e) {
            return false;
        }

    }


    public SignUpResponse signUp(SignUpRequest signUpRequest) {

        if (userRepository.existsByEmailAndDeactivatedAtIsNull(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use with a different account");
        }

        User user = User.builder()
                .email(signUpRequest.getEmail())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(UserRole.CUSTOMER)
                .build();

        User savedUser = userRepository.save(user);

        return SignUpResponse.builder()
                .message("Sign up complete. Please proceed to log in.")
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .build();


    }
}
