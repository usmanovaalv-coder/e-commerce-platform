package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.model.CreateUserRequest;
import com.ecommerce.userservice.model.CreateUserResponse;
import com.ecommerce.userservice.model.GetUserResponse;
import com.ecommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<GetUserResponse> getUserByUsername(@PathVariable String username) {
        GetUserResponse userResponse = userService.getUserByUsername(username);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    ResponseEntity<CreateUserResponse> create(@Valid @RequestBody CreateUserRequest userRequestDto) {
        CreateUserResponse userResponse = userService.create(userRequestDto);
        return ResponseEntity.ok(userResponse);
    }
}
