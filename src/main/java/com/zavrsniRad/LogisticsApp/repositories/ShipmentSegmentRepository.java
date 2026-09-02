package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentSegment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentSegmentRepository extends JpaRepository<ShipmentSegment, UUID> {

    boolean existsByTransportUnitId(UUID transportUnitId);

    @EntityGraph(attributePaths = {"transportUnit", "startLocation", "endLocation"})
    List<ShipmentSegment> findAllByShipmentId(UUID id);

    @EntityGraph(attributePaths = {"transportUnit", "startLocation", "endLocation"})
    Optional<ShipmentSegment> findByIdAndShipmentId(UUID id, UUID shipmentId);

    Optional<ShipmentSegment> findByShipmentIdAndSequenceNumber(UUID shipmentId, short i);
}
