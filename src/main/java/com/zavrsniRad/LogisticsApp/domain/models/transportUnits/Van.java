package com.zavrsniRad.LogisticsApp.domain.models.transportUnits;

import com.zavrsniRad.LogisticsApp.domain.enums.TransportUnitType;
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

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String model;

    @Override
    public TransportUnitType getType() {
        return TransportUnitType.VAN;
    }
}