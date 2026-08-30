package com.zavrsniRad.LogisticsApp.config;

import com.zavrsniRad.LogisticsApp.domain.enums.UserRole;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import com.zavrsniRad.LogisticsApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.admin.email:}")
    private String adminEmail;

    @Value("${app.bootstrap.admin.password:}")
    private String adminPassword;

    @Value("${app.bootstrap.admin.first-name:System}")
    private String adminFirstName;

    @Value("${app.bootstrap.admin.last-name:Administrator}")
    private String adminLastName;


    @Override
    public void run(ApplicationArguments args) {

        if (userRepository.countByRoleAndDeactivatedAtIsNull(UserRole.ADMIN) > 0) {
            return;
        }

        if (!StringUtils.hasText(adminEmail) || !StringUtils.hasText(adminPassword)) {
            log.warn("No active admin exists, but ADMIN_EMAIL/ADMIN_PASSWORD are not set in the .env "
                    + "Skipping admin bootstrap - admin-gated endpoints will be unreachable.");
            return;
        }

        if (userRepository.existsByEmailAndDeactivatedAtIsNull(adminEmail)) {
            log.warn("No active admin exists, but {} is already taken by an active non-admin account. "
                    + "Skipping admin bootstrap.", adminEmail);
            return;
        }

        User admin = User.builder()
                .email(adminEmail)
                .firstName(adminFirstName)
                .lastName(adminLastName)
                .password(passwordEncoder.encode(adminPassword))
                .role(UserRole.ADMIN)
                .build();

        User saved = userRepository.save(admin);

        log.info("No active admin found. Bootstrapped admin account {} with ID {}.",
                saved.getEmail(), saved.getId());

    }

}
