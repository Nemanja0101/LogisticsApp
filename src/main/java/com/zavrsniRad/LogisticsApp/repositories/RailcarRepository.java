package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.transportUnits.Railcar;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RailcarRepository extends JpaRepository<Railcar, UUID> {

    @Override
    @EntityGraph(attributePaths = "capabilities")
    List<Railcar> findAll();

    @EntityGraph(attributePaths = "capabilities")
    Optional<Railcar> findWithCapabilitiesById(UUID id);

    boolean existsByWagonNumber(String wagonNumber);

    boolean existsByWagonNumberAndIdNot(String wagonNumber, UUID id);

}
