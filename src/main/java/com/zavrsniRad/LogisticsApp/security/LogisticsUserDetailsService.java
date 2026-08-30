package com.zavrsniRad.LogisticsApp.security;

import com.zavrsniRad.LogisticsApp.domain.models.User;
import com.zavrsniRad.LogisticsApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@RequiredArgsConstructor
public class LogisticsUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user =userRepository.findByEmailAndDeactivatedAtIsNull(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found with email: " + email));

        return new LogisticsUserDetails(user);
    }
}
