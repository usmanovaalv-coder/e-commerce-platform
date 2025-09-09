package com.ecommerce.userservice.mapper;

import com.ecommerce.userservice.dto.UserDto;
import com.ecommerce.userservice.entity.UserEntity;
import com.ecommerce.userservice.model.GetClientResponse;
import com.ecommerce.userservice.model.GetUserResponse;
import com.ecommerce.userservice.model.CreateUserRequest;
import com.ecommerce.userservice.model.CreateUserResponse;
import com.ecommerce.userservice.params.UserParam;
import org.mapstruct.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {


    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "password", qualifiedByName = "encodePassword")
    UserEntity toUserEntity(CreateUserRequest createUserRequest);

    CreateUserResponse toUserResponse(UserEntity userEntity);

    GetUserResponse toGetUserResponse(UserEntity userEntity);

    @Mapping(target = "name", source = "userName")
    GetClientResponse toGetClientResponse(UserEntity userEntity);

    UserDto toUserDto(UserEntity userEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", qualifiedByName = "encodePassword")
    UserEntity toUserEntity(@MappingTarget UserEntity userEntity,
                            UserParam userParam);

    @Named("encodePassword")
    default String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
