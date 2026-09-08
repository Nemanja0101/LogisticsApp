package com.zavrsniRad.LogisticsApp.domain.models.transportUnits;


import com.zavrsniRad.LogisticsApp.domain.enums.TrailerType;
import com.zavrsniRad.LogisticsApp.domain.enums.TransportUnitType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "trucks")
@DiscriminatorValue(value = "TRUCK")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Truck extends TransportUnit{

    @Enumerated(EnumType.STRING)
    @Column(name = "trailer_type", nullable = false)
    private TrailerType trailerType;

    @Column(name = "axle_count", nullable = false)
    private Integer axleCount;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String model;


    @Override
    public TransportUnitType getType() {
        return TransportUnitType.TRUCK;
    }
}
