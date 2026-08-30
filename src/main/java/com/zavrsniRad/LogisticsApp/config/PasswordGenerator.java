package com.zavrsniRad.LogisticsApp.config;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class PasswordGenerator {

    private static final String LOWER   = "abcdefghijkmnpqrstuvwxyz";  // l
    private static final String UPPER   = "ABCDEFGHJKLMNPQRSTUVWXYZ";  // I, O
    private static final String DIGITS  = "23456789";                  // 0, 1
    private static final String SPECIAL = "@#$%^&+=!?";
    private static final String ALL = LOWER + UPPER + DIGITS + SPECIAL;
    private final SecureRandom secureRandom = new SecureRandom();


    public String generate(int length){

        if (length < 4) {
            throw new IllegalArgumentException("Password length must be at least 4");
        }

        ArrayList<Character> randoms = new ArrayList<>(
                Arrays.asList(
                        randomChar(LOWER),
                        randomChar(UPPER),
                        randomChar(DIGITS),
                        randomChar(SPECIAL)
                )
        );

        for (int i = 0; i < length - 4; i++){
            randoms.add(randomChar(ALL));
        }

        Collections.shuffle(randoms, secureRandom);
        return randoms.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private char randomChar(String alphabet){
        return alphabet.charAt(secureRandom.nextInt(alphabet.length()));
    }

}
