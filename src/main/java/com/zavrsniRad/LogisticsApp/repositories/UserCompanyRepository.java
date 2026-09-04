package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.UserCompany;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserCompany, UUID> {

    @EntityGraph(attributePaths = "user")
    List<UserCompany> findAllByCompanyId(UUID companyId);

    @EntityGraph(attributePaths = "user")
    Optional<UserCompany> findByCompanyIdAndUserId(UUID companyId, UUID userId);

    boolean existsByCompanyIdAndUserId(UUID companyId, UUID userId);

}
