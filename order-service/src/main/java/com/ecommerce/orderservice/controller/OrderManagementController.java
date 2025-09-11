package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.enums.OrderStatus;
import com.ecommerce.orderservice.exception.ApiError;
import com.ecommerce.orderservice.service.OrderManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Order Management", description = "Manager/admin operations for orders")
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(value = """
              {"code":"UNAUTHORIZED","message":"JWT is missing or invalid","path":"/api/v1/orders/manage","timestamp":"2025-09-11T12:00:00Z"}
            """))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(value = """
              {"code":"FORBIDDEN","message":"Insufficient authority","path":"/api/v1/orders/manage","timestamp":"2025-09-11T12:00:00Z"}
            """))
        )
})
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('MANAGER')")
@RequestMapping("/api/v1/orders/manage")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderManagementController {

    OrderManagementService orderManagementService;

    @Operation(
            summary = "Update order status",
            description = "Transitions the order to a new status. Business rules may restrict certain transitions."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Order status updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class),
                            examples = @ExampleObject(value = """
                  {
                    "id": 101,
                    "number": "ORD-2025-000101",
                    "status": "PROCESSING",
                    "totalAmount": 199.98,
                    "currency": "USD",
                    "items": [
                      {"productId": 1, "name": "Phone X", "price": 149.99, "quantity": 1},
                      {"productId": 2, "name": "Case", "price": 49.99, "quantity": 1}
                    ],
                    "updated": "2025-09-11T12:45:00Z"
                  }
                """))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"ILLEGAL_ARGUMENT","message":"Invalid status value","details":"status=FOO","path":"/api/v1/orders/manage/101/status","timestamp":"2025-09-11T12:00:00Z"}
                """))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"NOT_FOUND","message":"Order not found","details":"orderId=101","path":"/api/v1/orders/manage/101/status","timestamp":"2025-09-11T12:00:00Z"}
                """))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Invalid state transition / business conflict",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(value = """
                  {"code":"CONFLICT","message":"Order cannot transition from SHIPPED to CANCELED","path":"/api/v1/orders/manage/101/status","timestamp":"2025-09-11T12:00:00Z"}
                """))
            )
    })
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable long orderId,
                                                      @RequestParam OrderStatus status) {
        OrderDto updatedOrder = orderManagementService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
}
