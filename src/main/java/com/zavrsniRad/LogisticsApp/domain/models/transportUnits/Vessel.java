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
@Table(name = "vessels")
@DiscriminatorValue(value = "VESSEL")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Vessel extends TransportUnit {

    @Column(name = "imo_number", nullable = false, unique = true)
    private String imoNumber;

    @Column(name = "port_of_registry")
    private String portOfRegistry;

    @Column(name = "operated_by")
    private String operatedBy;

    @Override
    public TransportUnitType getType() {
        return TransportUnitType.VESSEL;
    }
}