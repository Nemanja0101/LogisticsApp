package com.zavrsniRad.LogisticsApp.mappers;

import com.zavrsniRad.LogisticsApp.domain.dto.user.CreateUserResponse;
import com.zavrsniRad.LogisticsApp.domain.dto.user.UserResponse;
import com.zavrsniRad.LogisticsApp.domain.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {


    UserResponse toResponse(User user);

    @Mapping(target = "temporaryPassword", source="temporaryPassword")
    CreateUserResponse toCreateUserResponse(User user, String temporaryPassword);


}
