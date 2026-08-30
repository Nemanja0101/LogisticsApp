package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.shipment.Freight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FreightRepository extends JpaRepository<Freight, UUID> {

    @Query("""
            SELECT f.shipmentOrder.id AS orderId, COUNT(f) AS COUNT
            FROM Freight f WHERE f.shipmentOrder.id IN :orderIds
            GROUP BY f.shipmentOrder.id
            """)
    List<Object[]> countByShipmentOrderIds(@Param("orderIds") List<UUID> orderIds);

    long countByShipmentOrderId(UUID shipmentOrderId);

    List<Freight> findAllByShipmentOrderId(UUID shipmentOrderId);

    Optional<Freight> findByIdAndShipmentOrderId(UUID id, UUID shipmentOrderId);

}