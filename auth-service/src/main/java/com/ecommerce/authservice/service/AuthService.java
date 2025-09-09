package com.ecommerce.authservice.service;

import com.ecommerce.authservice.dto.UserDto;
import com.ecommerce.authservice.param.UserParam;

public interface AuthService {

    UserDto register(UserParam userParam);

}
