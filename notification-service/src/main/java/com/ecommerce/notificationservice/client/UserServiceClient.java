package com.ecommerce.notificationservice.client;

import com.ecommerce.notificationservice.model.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/api/v1/internal/clients")
public interface UserServiceClient {

    @GetMapping("/by-id/{id}")
    ClientResponse getClientById(@PathVariable long id);

}
