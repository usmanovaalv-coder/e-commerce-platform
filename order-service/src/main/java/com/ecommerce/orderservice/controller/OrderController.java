package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.exception.ApiError;
import com.ecommerce.orderservice.param.CreateOrderParams;
import com.ecommerce.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Orders", description = "Create and manage customer orders")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(value = """
              {"code":"UNAUTHORIZED","message":"JWT is missing or invalid","path":"/api/v1/orders","timestamp":"2025-09-11T12:00:00Z"}
            """))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(value = """
              {"code":"FORBIDDEN","message":"Insufficient authority","path":"/api/v1/orders","timestamp":"2025-09-11T12:00:00Z"}
            """))
        )
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@PreAuthorize("hasAuthority('CLIENT')")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @Operation(
            summary = "Create order",
            description = "Creates a new order for the authenticated user (username is taken from JWT `sub`)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Order created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class),
                            examples = @ExampleObject(value = """
                  {
                    "id": 101,
                    "number": "ORD-2025-000101",
                    "status": "NEW",
                    "totalAmount": 199.98,
                    "currency": "USD",
                    "items": [
                      {"productId": 1, "name": "Phone X", "price": 149.99, "quantity": 1},
                      {"productId": 2, "name": "Case", "price": 49.99, "quantity": 1}
                    ],
                    "created": "2025-09-11T12:34:56Z"
                  }
                """))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"VALIDATION_ERROR","message":"Validation failed","details":"items[0].quantity: must be >= 1","path":"/api/v1/orders","timestamp":"2025-09-11T12:00:00Z"}
                """))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product or user not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"NOT_FOUND","message":"Product not found","details":"productId=999","path":"/api/v1/orders","timestamp":"2025-09-11T12:00:00Z"}
                """))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Insufficient stock / business conflict",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"CONFLICT","message":"Insufficient stock","details":"productId=1, requested=5, available=2","path":"/api/v1/orders","timestamp":"2025-09-11T12:00:00Z"}
                """))
            )
    })
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid CreateOrderParams orderParams,
                                                @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        OrderDto order = orderService.createOrder(orderParams, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @Operation(
            summary = "Cancel order",
            description = "Cancels an order by id (only allowed for certain statuses, e.g. NEW/PROCESSING)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Order canceled (idempotent)"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid id",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Order cannot be canceled due to status",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"CONFLICT","message":"Order cannot be canceled","details":"status=SHIPPED","path":"/api/v1/orders/id","timestamp":"2025-09-11T12:00:00Z"}
                """))
            )
    })
    @PostMapping("/id")
    public ResponseEntity<Void> cancelOrder(@RequestParam long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }
}
