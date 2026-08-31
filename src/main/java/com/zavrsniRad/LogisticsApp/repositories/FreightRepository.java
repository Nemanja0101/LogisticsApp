package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.shipment.Freight;
import org.springframework.data.jpa.repository.EntityGraph;
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


    @EntityGraph(attributePaths = "shipmentOrder")
    @Query("SELECT f FROM Freight f WHERE id = :id")
    Optional<Freight> findByIdWithShipmentOrder(@Param("id") UUID id);


    @Query("""
        SELECT f
        FROM Freight f
        LEFT JOIN FETCH f.shipmentOrder so
        LEFT JOIN FETCH f.shipment s
        WHERE f.id = :id AND s.id = :sid
        """)
    Optional<Freight> findByIdWithShipmentOrderAndShipment(@Param("id") UUID id, @Param("sid") UUID shipmentId);


    long countByShipmentOrderId(UUID shipmentOrderId);

    List<Freight> findAllByShipmentOrderId(UUID shipmentOrderId);
    List<Freight> findAllByShipmentId(UUID shipmentId);

    Optional<Freight> findByIdAndShipmentOrderId(UUID id, UUID shipmentOrderId);

}