package com.ecommerce.orderservice.client;

import com.ecommerce.orderservice.model.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/api/v1/clients")
public interface UserServiceClient {

    @GetMapping("/by-name/{username}")
    ClientResponse getClientByUsername(@PathVariable String username);

    @GetMapping("/by-id/{id}")
    ClientResponse getClientById(@PathVariable long id);

}
