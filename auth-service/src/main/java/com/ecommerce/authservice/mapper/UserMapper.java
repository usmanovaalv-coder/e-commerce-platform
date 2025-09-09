package com.ecommerce.authservice.mapper;

import com.ecommerce.authservice.dto.UserDto;
import com.ecommerce.authservice.model.RegisterUserRequest;
import com.ecommerce.authservice.model.RegisterUserResponse;
import com.ecommerce.authservice.param.UserParam;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {

    RegisterUserRequest toUserRequest(UserParam userParam);

    UserDto toUserResponseDto(RegisterUserResponse registerUserResponse);
}
