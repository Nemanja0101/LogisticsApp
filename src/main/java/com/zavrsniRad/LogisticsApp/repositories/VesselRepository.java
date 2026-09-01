package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Vessel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VesselRepository extends JpaRepository<Vessel, UUID> {

    @Override
    @EntityGraph(attributePaths = "capabilities")
    List<Vessel> findAll();

    @EntityGraph(attributePaths = "capabilities")
    Optional<Vessel> findWithCapabilitiesById(UUID id);

    boolean existsByImoNumber(String imoNumber);

    boolean existsByImoNumberAndIdNot(String imoNumber, UUID id);

}
