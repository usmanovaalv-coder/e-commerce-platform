package com.ecommerce.userservice.service;

import com.ecommerce.userservice.model.GetClientResponse;
import com.ecommerce.userservice.model.GetUserResponse;
import com.ecommerce.userservice.model.CreateUserRequest;
import com.ecommerce.userservice.model.CreateUserResponse;

public interface UserService {

    GetUserResponse getUserByUsername(String username);

    GetClientResponse getById(long id);

    GetClientResponse getClientByUsername(String username);

    CreateUserResponse create(CreateUserRequest createUserRequest);
}
