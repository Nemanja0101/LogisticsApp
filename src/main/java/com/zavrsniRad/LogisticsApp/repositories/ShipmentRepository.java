package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.shipment.Shipment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

    @EntityGraph(attributePaths = "freights")
    @Query("SELECT s FROM Shipment s")
    List<Shipment> findAllWithFreights();


    @EntityGraph(attributePaths = "freights")
    @Query("SELECT s FROM Shipment s WHERE s.id = :id")
    Optional<Shipment> findByIdAndFetchFreights(@Param("id") UUID id);
}
