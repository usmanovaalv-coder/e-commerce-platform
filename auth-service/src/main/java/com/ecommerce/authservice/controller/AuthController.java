package com.ecommerce.authservice.controller;

import com.ecommerce.authservice.dto.UserDto;
import com.ecommerce.authservice.exception.ApiError;
import com.ecommerce.authservice.param.UserParam;
import com.ecommerce.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@Tag(name = "Auth", description = "User registration and authentication")
@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @Operation(
            summary = "Register new user",
            description = "Public endpoint. Creates a new user account and returns public user data."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(value = """
                  {"id":1,"username":"alice","email":"alice@example.com","role":"USER"}
                """))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"VALIDATION_ERROR","message":"Validation failed","details":"username: must not be blank, password: must not be blank","path":"/api/v1/auth/register","timestamp":"2025-09-11T12:00:00Z"}
                """))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists (username/email conflict)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"USER_ALREADY_EXISTS","message":"User with given username or email already exists","details":"username=alice","path":"/api/v1/auth/register","timestamp":"2025-09-11T12:00:00Z"}
                """))
            )
    })
    @PostMapping("/register")
    ResponseEntity<UserDto> register(@Valid @RequestBody UserParam userParam) {
        UserDto userDto = authService.register(userParam);
        return ResponseEntity.ok(userDto);
    }

}
