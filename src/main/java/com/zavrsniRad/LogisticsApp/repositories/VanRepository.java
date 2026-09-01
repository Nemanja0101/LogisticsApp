package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Van;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VanRepository extends JpaRepository<Van, UUID> {

    @Override
    @EntityGraph(attributePaths = "capabilities")
    List<Van> findAll();

    @EntityGraph(attributePaths = "capabilities")
    Optional<Van> findWithCapabilitiesById(UUID id);

    boolean existsByLicensePlate(String licensePlate);

    boolean existsByLicensePlateAndIdNot(String licensePlate, UUID id);

}
