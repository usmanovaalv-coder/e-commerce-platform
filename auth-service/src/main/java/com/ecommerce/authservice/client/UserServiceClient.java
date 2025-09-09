package com.ecommerce.authservice.client;

import com.ecommerce.authservice.model.GetUserResponse;
import com.ecommerce.authservice.model.RegisterUserRequest;
import com.ecommerce.authservice.model.RegisterUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", path = "/api/v1/users")
public interface UserServiceClient {

    @GetMapping("/{username}")
    GetUserResponse getUser(@PathVariable String username);

    @PostMapping
    RegisterUserResponse createUser(@RequestBody RegisterUserRequest registerUserRequest);

}
