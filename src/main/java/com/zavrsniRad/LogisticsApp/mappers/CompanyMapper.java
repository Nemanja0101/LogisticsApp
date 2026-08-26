package com.zavrsniRad.LogisticsApp.mappers;

import com.zavrsniRad.LogisticsApp.domain.dto.company.CompanyResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.company.CreateUpdateCompany;
import com.zavrsniRad.LogisticsApp.domain.dto.company.SingleCompanyResponse;
import com.zavrsniRad.LogisticsApp.domain.models.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = CompanyMemberMapper.class)

public interface CompanyMapper {

    CompanyResponse toResponse(Company company);

    @Mapping(target = "members", source = "memberships")
    SingleCompanyResponse toSingleCompany(Company company);

    Company toEntity(CreateUpdateCompany createCompany);


}
