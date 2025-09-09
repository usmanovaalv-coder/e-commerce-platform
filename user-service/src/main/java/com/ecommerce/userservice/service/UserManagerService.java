package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.UserDto;
import com.ecommerce.userservice.params.UserParam;

import java.util.List;

public interface UserManagerService {

    UserDto getByUsername(String username);

    List<UserDto> getUsers(int limit, int offset);

    UserDto updateUser(long id, UserParam userParam);

    void deleteUser(long id);
}
