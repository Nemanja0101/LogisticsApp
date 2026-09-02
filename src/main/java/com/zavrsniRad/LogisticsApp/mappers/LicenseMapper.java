package com.zavrsniRad.LogisticsApp.mappers;

import com.zavrsniRad.LogisticsApp.domain.dto.crew.LicenseDto;
import com.zavrsniRad.LogisticsApp.domain.models.crew.License;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LicenseMapper {

    License toEntity(LicenseDto licenseDto);

}
