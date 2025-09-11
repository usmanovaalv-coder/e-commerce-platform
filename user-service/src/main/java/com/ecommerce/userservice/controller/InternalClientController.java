package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.model.GetClientResponse;
import com.ecommerce.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/internal/clients")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class InternalClientController {

    UserService userService;

    @GetMapping("/by-id/{id}")
    public ResponseEntity<GetClientResponse> getClientById(@PathVariable long id) {
        GetClientResponse userResponse = userService.getById(id);
        return ResponseEntity.ok(userResponse);
    }
}
