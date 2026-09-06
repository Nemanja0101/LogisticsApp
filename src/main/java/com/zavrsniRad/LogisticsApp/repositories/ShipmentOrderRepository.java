package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentOrderRepository extends JpaRepository<ShipmentOrder, UUID> {

    @EntityGraph(attributePaths = {"startLocation", "endLocation"})
    Page<ShipmentOrder> findAllByCustomerId(UUID customerId, Pageable pageable);
    Optional<ShipmentOrder> findByIdAndCustomerId(UUID id, UUID customerId);

    @Override
    @EntityGraph(attributePaths = {"startLocation", "endLocation"})
    Page<ShipmentOrder> findAll(Pageable pageable);
}
