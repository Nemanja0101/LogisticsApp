package com.zavrsniRad.LogisticsApp.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "freights")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Freight {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_order_id", nullable = false)
    private ShipmentOrder shipmentOrder;

    @ManyToOne
    @JoinColumn(name = "shipment_id", nullable = true)
    private Shipment shipment;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }


}
