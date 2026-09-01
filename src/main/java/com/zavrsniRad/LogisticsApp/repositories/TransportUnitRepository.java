package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.TransportUnit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransportUnitRepository extends JpaRepository<TransportUnit, UUID> {

    @Override
    @EntityGraph(attributePaths = "capabilities")
    List<TransportUnit> findAll();

    @EntityGraph(attributePaths = "capabilities")
    Optional<TransportUnit> findWithCapabilitiesById(UUID id);

}
