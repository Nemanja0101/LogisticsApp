package com.zavrsniRad.LogisticsApp.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shipment_segments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShipmentSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    //koji je po redu u ruti
    @Column(name = "sequence_number", nullable = false)
    private Short sequenceNumber;


    //TODO: implement a Transport Unit class
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private TransportMode transportMode; // TRUCK, SHIP, RAIL, AIR

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_location_id", nullable = false)
    private Location startLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_location_id", nullable = false)
    private Location endLocation;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

}
