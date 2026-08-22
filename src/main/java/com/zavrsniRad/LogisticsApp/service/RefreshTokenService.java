package com.zavrsniRad.LogisticsApp.service;


import com.zavrsniRad.LogisticsApp.domain.models.RefreshToken;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import com.zavrsniRad.LogisticsApp.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();


    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMs;

    public String generate(User user){

        String rawToken = generateOpaqueToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenHash(hash(rawToken))
                .expiresAt(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
        return rawToken;

    }

    public RefreshToken validate(String rawToken){

        RefreshToken stored = refreshTokenRepository.findByTokenHash(hash(rawToken))
                .orElseThrow(()-> new BadCredentialsException("Invalid refresh token"));

        if(stored.isRevoked() || stored.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new BadCredentialsException("Refresh token expired or revoked");
        }

        return stored;
    }


    public void revoke(RefreshToken refreshToken){

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

    }


    private String generateOpaqueToken(){

        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

    }

    private String hash(String rawToken){

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(digest.digest(rawToken.getBytes()));

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }


    }

}
