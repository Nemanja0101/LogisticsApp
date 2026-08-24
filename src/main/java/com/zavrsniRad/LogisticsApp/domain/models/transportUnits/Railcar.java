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
@Table(name = "railcars")
@DiscriminatorValue(value = "RAILCAR")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Railcar extends TransportUnit{

    @Column(name = "wagon_number", nullable = false, unique = true)
    private String wagonNumber;

    @Column(name = "operated_by", nullable = false)
    private String operatedBy;
}
