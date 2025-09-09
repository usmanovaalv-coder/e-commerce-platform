package com.ecommerce.authservice.service.impl;

import com.ecommerce.authservice.client.UserServiceClient;
import com.ecommerce.authservice.dto.UserDto;
import com.ecommerce.authservice.mapper.UserMapper;
import com.ecommerce.authservice.model.RegisterUserRequest;
import com.ecommerce.authservice.model.RegisterUserResponse;
import com.ecommerce.authservice.param.UserParam;
import com.ecommerce.authservice.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserServiceClient userServiceClient;
    UserMapper userMapper;

    @Override
    public UserDto register(UserParam userParam) {
        RegisterUserRequest registerUserRequest = userMapper.toUserRequest(userParam);

        log.info("Sending user registration request to UserService for username: {}", userParam.getUserName());
        RegisterUserResponse registerUserResponse = userServiceClient.createUser(registerUserRequest);
        log.info("Received response from UserService for username: {}", userParam.getUserName());

        return userMapper.toUserResponseDto(registerUserResponse);
    }
}
