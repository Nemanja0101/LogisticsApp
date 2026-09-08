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
@Table(name = "aircraft")
@DiscriminatorValue(value = "AIRCRAFT")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Aircraft extends TransportUnit {

    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String model;

    @Column(name = "max_range_km", nullable = false)
    private Integer maxRangeKm;

    @Column(name = "operated_by")
    private String operatedBy;

    @Override
    public TransportUnitType getType() {
        return TransportUnitType.AIRCRAFT;
    }
}
