package com.zavrsniRad.LogisticsApp.domain.dto.shipment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResponse {

    private UUID id;
    private String name;
    private String address;
    private String city;
    private String country;
    private Double latitude;
    private Double longitude;

}
