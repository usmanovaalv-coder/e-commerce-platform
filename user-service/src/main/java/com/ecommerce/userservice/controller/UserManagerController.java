package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.dto.UserDto;
import com.ecommerce.userservice.exception.ApiError;
import com.ecommerce.userservice.params.UserParam;
import com.ecommerce.userservice.service.UserManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Tag(name = "User Management", description = "Admin/manager endpoints to manage users")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(value = """
              {"code":"UNAUTHORIZED","message":"JWT is missing or invalid","path":"/api/v1/users-manager","timestamp":"2025-09-11T12:00:00Z"}
            """))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(value = """
              {"code":"FORBIDDEN","message":"Insufficient authority","path":"/api/v1/users-manager","timestamp":"2025-09-11T12:00:00Z"}
            """))
        )
})
@RestController
@RequestMapping(value = "/api/v1/users-manager")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserManagerController {

    UserManagerService userManagerService;

    @Operation(
            summary = "Get user by username",
            description = "Returns user details by username. Accessible for roles ADMIN and USER."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(value = """
                  {"id":2,"username":"jdoe","email":"jdoe@example.com","roles":["USER"],"created":"2025-01-10T09:15:30Z"}
                """))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"NOT_FOUND","message":"User not found","details":"username=jdoe","path":"/api/v1/users-manager/jdoe","timestamp":"2025-09-11T12:00:00Z"}
                """))
            )
    })
    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        UserDto userDto = userManagerService.getByUsername(username);
        return ResponseEntity.ok(userDto);
    }

    @Operation(
            summary = "List users",
            description = "Returns a page-like slice of users using limit/offset."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Users list",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class)),
                            examples = @ExampleObject(value = """
                  [
                    {"id":1,"username":"alice","email":"alice@example.com","roles":["ADMIN"]},
                    {"id":2,"username":"jdoe","email":"jdoe@example.com","roles":["USER"]}
                  ]
                """))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination parameters",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"ILLEGAL_ARGUMENT","message":"Invalid limit/offset","details":"limit must be > 0","path":"/api/v1/users-manager","timestamp":"2025-09-11T12:00:00Z"}
                """))
            )
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(defaultValue = "10") int limit,
                                                  @RequestParam(defaultValue = "0") int offset) {
        List<UserDto> userDtos = userManagerService.getUsers(limit, offset);
        return ResponseEntity.ok(userDtos);
    }

    @Operation(
            summary = "Update user",
            description = "Updates user fields by id. Accessible only for ADMIN."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(value = """
                  {"id":5,"username":"maria","email":"maria@example.com","roles":["USER"],"updated":"2025-09-11T13:00:00Z"}
                """))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict (e.g. username/email already in use)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"CONFLICT","message":"Email already in use","details":"email=maria@example.com","path":"/api/v1/users-manager/5","timestamp":"2025-09-11T12:00:00Z"}
                """))
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable long id,
                                              @RequestBody UserParam userParam) {
        UserDto userDto = userManagerService.updateUser(id, userParam);
        return ResponseEntity.ok(userDto);
    }

    @Operation(
            summary = "Delete user",
            description = "Deletes a user by id. Accessible only for ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userManagerService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
