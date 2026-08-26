package com.zavrsniRad.LogisticsApp.mappers;

import com.zavrsniRad.LogisticsApp.domain.dto.company.member.CompanyMemberResponse;
import com.zavrsniRad.LogisticsApp.domain.models.UserCompany;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMemberMapper {


    @Mapping(target = "email", source = "userCompany.user.email")
    @Mapping(target = "firstName", source = "userCompany.user.firstName")
    @Mapping(target = "lastName", source = "userCompany.user.lastName")
    @Mapping(target = "userId", source = "userCompany.user.id")
    CompanyMemberResponse toResponse(UserCompany userCompany);


}
