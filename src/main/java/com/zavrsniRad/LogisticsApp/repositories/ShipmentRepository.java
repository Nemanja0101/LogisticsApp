package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.shipment.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

    @Override
    Page<Shipment> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "freights")
    @Query("SELECT s FROM Shipment s WHERE s.id = :id")
    Optional<Shipment> findByIdAndFetchFreights(@Param("id") UUID id);

    @Query("""
        SELECT s
        FROM Shipment s
        LEFT JOIN FETCH s.freights f
        LEFT JOIN FETCH f.shipmentOrder so
        WHERE s.id = :id
""")
    Optional<Shipment> findByIdAndFetchFreightsAndShipmentOrder(@Param("id") UUID id);

}
