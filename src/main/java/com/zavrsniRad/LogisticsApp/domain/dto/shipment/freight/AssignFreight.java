package com.zavrsniRad.LogisticsApp.domain.dto.shipment.freight;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignFreight {

    @NotNull
    private UUID id;

}
