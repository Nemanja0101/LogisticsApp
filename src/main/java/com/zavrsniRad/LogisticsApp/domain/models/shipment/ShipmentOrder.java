package com.zavrsniRad.LogisticsApp.domain.models.shipment;

import com.zavrsniRad.LogisticsApp.domain.enums.ShipmentOrderStatus;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shipment_orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShipmentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_location_id", nullable = false)
    private Location startLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_location_id", nullable = false)
    private Location endLocation;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ShipmentOrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

//  cascade acts on the side it's created, meaning
//  whatever happens on my side (ShipmentOrder) cascade to the objects
//  in my collections, in this case, if i save myself, persist the objects
//  in the list to the DB also.

//    DECLARE FETCH TYPE EAGER HERE IF THE FREIGHTS ARE NOT BEING SAVED/FETCHED CORRECTLY.
    @OneToMany(mappedBy = "shipmentOrder",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private List<Freight> freights = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }
}
