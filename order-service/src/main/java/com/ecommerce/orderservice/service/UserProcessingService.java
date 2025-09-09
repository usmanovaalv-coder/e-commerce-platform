package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.client.UserServiceClient;
import com.ecommerce.orderservice.dto.UserDto;
import com.ecommerce.orderservice.mapper.OrderMapper;
import com.ecommerce.orderservice.model.ClientResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProcessingService {

    UserServiceClient userServiceClient;
    OrderMapper orderMapper;

    public UserDto fetchClientData(String username) {
        log.debug("Fetching client data for user: {}", username);
        ClientResponse response = userServiceClient.getClientByUsername(username);
        log.debug("Fetched client data for user: {}, data: {}", username, response);

        return orderMapper.toUserDto(response);
    }

    public UserDto fetchClientData(long userId) {
        log.debug("Fetching client data by userId: {}", userId);
        ClientResponse response = userServiceClient.getClientById(userId);
        log.debug("Fetched client data for userId: {}, data: {}", userId, response);

        return orderMapper.toUserDto(response);
    }
}
