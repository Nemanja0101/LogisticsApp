package com.zavrsniRad.LogisticsApp.repositories;

import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShipmentOrderRepository extends JpaRepository<ShipmentOrder, UUID> {


}
