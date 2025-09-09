package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.param.CreateOrderParams;
import com.ecommerce.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@PreAuthorize("hasAuthority('CLIENT')")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid CreateOrderParams orderParams,
                                                @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        OrderDto order = orderService.createOrder(orderParams, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/id")
    public ResponseEntity<Void> cancelOrder(@RequestParam long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }
}
