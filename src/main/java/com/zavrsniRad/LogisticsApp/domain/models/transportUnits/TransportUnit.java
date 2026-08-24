package com.zavrsniRad.LogisticsApp.domain.models.transportUnits;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transport_units")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "transport_type")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class TransportUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public String manufacturer;

    public String model;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

}


