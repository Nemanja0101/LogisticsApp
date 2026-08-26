package com.zavrsniRad.LogisticsApp.domain.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatchLocation {

    private String name;

    private String address;

    private String city;

    private String country;

    private Double latitude;

    private Double longitude;
}
