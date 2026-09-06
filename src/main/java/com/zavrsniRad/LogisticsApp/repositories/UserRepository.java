package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.enums.UserRole;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailAndDeactivatedAtIsNull(String email);
    boolean existsByEmailAndDeactivatedAtIsNull(String email);
    long countByRoleAndDeactivatedAtIsNull(UserRole role);
    Page<User> findAllByDeactivatedAtIsNull(Pageable pageable);
    Optional<User> findByIdAndDeactivatedAtIsNull(UUID id);
}
