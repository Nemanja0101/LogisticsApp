package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Aircraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AircraftRepository extends JpaRepository<Aircraft, UUID> {

    @Override
    Page<Aircraft> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "capabilities")
    Optional<Aircraft> findWithCapabilitiesById(UUID id);

    boolean existsByRegistrationNumber(String regNum);

    boolean existsByRegistrationNumberAndIdNot(String regNum, UUID id);

}
