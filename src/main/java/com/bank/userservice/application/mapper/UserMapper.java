package com.bank.userservice.application.mapper;

import com.bank.userservice.domain.dto.request.CreateUserRequest;
import com.bank.userservice.domain.dto.response.CreateUserResponse;
import com.bank.userservice.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.ZonedDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", source = "password")
    @Mapping(target = "createdAt", expression = "java(java.time.ZonedDateTime.now())")
    User toUser(CreateUserRequest request);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    CreateUserResponse toCreateUserResponse(User user);
}
