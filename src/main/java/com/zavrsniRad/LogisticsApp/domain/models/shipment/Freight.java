package com.zavrsniRad.LogisticsApp.domain.models.shipment;

import com.zavrsniRad.LogisticsApp.domain.enums.Capability;
import com.zavrsniRad.LogisticsApp.domain.enums.FreightForm;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "form", nullable = false)
    private FreightForm form;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "freight_requirements",
            joinColumns = @JoinColumn(name = "freight_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "capability", nullable = false)
    @Builder.Default
    private Set<Capability> requirements = new HashSet<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(name = "weight_kg", nullable = false)
    private Double weightKg;

    @Column(name = "length_cm", nullable = false)
    private Double lengthCm;

    @Column(name = "width_cm", nullable = false)
    private Double widthCm;

    @Column(name = "height_cm", nullable = false)
    private Double heightCm;


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
