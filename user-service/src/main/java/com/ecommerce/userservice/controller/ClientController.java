package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.exception.ApiError;
import com.ecommerce.userservice.model.GetClientResponse;
import com.ecommerce.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@Tag(name = "Clients", description = "Client lookup endpoints")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(value = """
              {"code":"UNAUTHORIZED","message":"JWT is missing or invalid","path":"/api/v1/clients","timestamp":"2025-09-11T12:00:00Z"}
            """))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(value = """
              {"code":"FORBIDDEN","message":"Insufficient authority","path":"/api/v1/clients","timestamp":"2025-09-11T12:00:00Z"}
            """))
        )
})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/clients")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@PreAuthorize("hasAuthority('CLIENT') or hasAuthority('MANAGER')")
public class ClientController {

    UserService userService;

    @Operation(
            summary = "Get client by username",
            description = "Returns client profile by username. Requires role CLIENT or MANAGER."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Client found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetClientResponse.class),
                            examples = @ExampleObject(value = """
                  {"id":2,"username":"jdoe","email":"jdoe@example.com","fullName":"John Doe","roles":["CLIENT"],"created":"2025-01-10T09:15:30Z"}
                """))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"NOT_FOUND","message":"Client not found","details":"username=jdoe","path":"/api/v1/clients/by-name/jdoe","timestamp":"2025-09-11T12:00:00Z"}
                """))
            )
    })
    @GetMapping("/by-name/{username}")
    public ResponseEntity<GetClientResponse> getClientByUsername(@PathVariable String username) {
        GetClientResponse userResponse = userService.getClientByUsername(username);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(
            summary = "Get client by id",
            description = "Returns client profile by numeric id. Requires role CLIENT or MANAGER."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Client found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetClientResponse.class),
                            examples = @ExampleObject(value = """
                  {"id":2,"username":"jdoe","email":"jdoe@example.com","fullName":"John Doe","roles":["CLIENT"],"created":"2025-01-10T09:15:30Z"}
                """))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"NOT_FOUND","message":"Client not found","details":"id=999","path":"/api/v1/clients/by-id/999","timestamp":"2025-09-11T12:00:00Z"}
                """))
            )
    })
    @GetMapping("/by-id/{id}")
    public ResponseEntity<GetClientResponse> getClientById(@PathVariable long id) {
        GetClientResponse userResponse = userService.getById(id);
        return ResponseEntity.ok(userResponse);
    }
}
