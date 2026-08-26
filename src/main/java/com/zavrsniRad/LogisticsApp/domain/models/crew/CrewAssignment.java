package com.zavrsniRad.LogisticsApp.domain.models.crew;


import com.zavrsniRad.LogisticsApp.domain.models.shipment.ShipmentSegment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "crew_assignments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CrewAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_member_id", nullable = false)
    private CrewMember crewMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_segment_id", nullable = false)
    private ShipmentSegment shipmentSegment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

}
