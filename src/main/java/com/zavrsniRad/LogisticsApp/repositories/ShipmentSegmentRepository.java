package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShipmentSegmentRepository extends JpaRepository<ShipmentSegment, UUID> {

    boolean existsByTransportUnitId(UUID transportUnitId);

}
