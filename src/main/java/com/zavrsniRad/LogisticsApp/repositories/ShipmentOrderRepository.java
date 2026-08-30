package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentOrderRepository extends JpaRepository<ShipmentOrder, UUID> {

    @Query("""
            SELECT o FROM ShipmentOrder o
            JOIN FETCH o.startLocation
            JOIN FETCH o.endLocation
            WHERE o.customer.id = :customerId
            """)
    List<ShipmentOrder> findAllByCustomerId(@Param("customerId") UUID customerId);
    Optional<ShipmentOrder> findByIdAndCustomerId(UUID id, UUID customerId);

    @EntityGraph(attributePaths = {"startLocation", "endLocation"})
    @Query("SELECT o FROM ShipmentOrder o")
    List<ShipmentOrder> findAllWithLocations();
}
