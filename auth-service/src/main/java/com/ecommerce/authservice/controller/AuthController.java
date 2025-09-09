package com.ecommerce.authservice.controller;

import com.ecommerce.authservice.dto.UserDto;
import com.ecommerce.authservice.param.UserParam;
import com.ecommerce.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    ResponseEntity<UserDto> register(@Valid @RequestBody UserParam userParam) {
        UserDto userDto = authService.register(userParam);
        return ResponseEntity.ok(userDto);
    }

}
