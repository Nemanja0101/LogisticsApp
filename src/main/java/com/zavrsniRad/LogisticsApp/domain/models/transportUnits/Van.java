package com.zavrsniRad.LogisticsApp.domain.models.transportUnits;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "vans")
@DiscriminatorValue(value = "VAN")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Van extends TransportUnit {

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "max_weight_kg", nullable = false)
    private Integer maxWeightKg;

    @Column(name = "cargo_volume_m3", nullable = false)
    private Double cargoVolumeM3;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String model;
}