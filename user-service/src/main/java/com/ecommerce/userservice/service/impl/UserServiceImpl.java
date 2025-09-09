package com.ecommerce.userservice.service.impl;

import com.ecommerce.userservice.entity.UserEntity;
import com.ecommerce.userservice.exception.UserAlreadyExistsException;
import com.ecommerce.userservice.mapper.UserMapper;
import com.ecommerce.userservice.model.CreateUserRequest;
import com.ecommerce.userservice.model.GetClientResponse;
import com.ecommerce.userservice.model.GetUserResponse;
import com.ecommerce.userservice.model.CreateUserResponse;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserMapper userMapper;
    UserRepository userRepository;

    @Override
    public GetUserResponse getUserByUsername(String username) {
        return userRepository.findByUserName(username)
                .map(userMapper::toGetUserResponse)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found"));
    }

    @Override
    public GetClientResponse getClientByUsername(String username) {
        return userRepository.findByUserName(username)
                .map(userMapper::toGetClientResponse)
                .orElseThrow(() -> new UsernameNotFoundException("Client with username '" + username + "' not found"));
    }

    @Override
    public GetClientResponse getById(long id) {
        return userRepository.findById(id)
                .map(userMapper::toGetClientResponse)
                .orElseThrow(() -> new UsernameNotFoundException("User with id '" + id + "' not found"));
    }

    @Override
    public CreateUserResponse create(CreateUserRequest createUserRequest) {
        String username = createUserRequest.getUserName();
        if (userRepository.existsByUserName(username)) {
            throw new UserAlreadyExistsException(username);
        }

        UserEntity userEntity = userMapper.toUserEntity(createUserRequest);
        userEntity = userRepository.save(userEntity);
        log.info("User saved to database with username: {}", userEntity.getUserName());

        return userMapper.toUserResponse(userEntity);
    }
}
