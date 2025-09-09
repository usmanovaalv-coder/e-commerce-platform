package com.ecommerce.userservice.controller;

import com.ecommerce.userservice.model.GetClientResponse;
import com.ecommerce.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/clients")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@PreAuthorize("hasAuthority('CLIENT') or hasAuthority('MANAGER')")
public class ClientController {

    UserService userService;

    @GetMapping("/by-name/{username}")
    public ResponseEntity<GetClientResponse> getClientByUsername(@PathVariable String username) {
        GetClientResponse userResponse = userService.getClientByUsername(username);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<GetClientResponse> getClientById(@PathVariable long id) {
        GetClientResponse userResponse = userService.getById(id);
        return ResponseEntity.ok(userResponse);
    }
}
