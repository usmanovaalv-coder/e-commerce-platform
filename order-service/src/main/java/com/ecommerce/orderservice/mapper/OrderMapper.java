package com.ecommerce.orderservice.mapper;

import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.dto.OrderItemDto;
import com.ecommerce.orderservice.dto.UserDto;
import com.ecommerce.orderservice.entity.OrderEntity;
import com.ecommerce.orderservice.entity.OrderItemEntity;
import com.ecommerce.orderservice.model.ClientResponse;
import com.ecommerce.orderservice.model.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface OrderMapper {

    @Mapping(target = "productId", source = "response.id")
    @Mapping(target = "productName", source = "response.name")
    @Mapping(target = "price", source = "response.price")
    @Mapping(target = "quantity", source = "quantity")
    OrderItemEntity toOrderItemEntity(ProductResponse response, int quantity);

    @Mapping(target = "id", source = "order.id")
    @Mapping(target = "user", source = "userDto")
    OrderDto toOrderDto(OrderEntity order, UserDto userDto);

    OrderItemDto toOrderItemDto(OrderItemEntity orderItemEntity);

    UserDto toUserDto(ClientResponse clientResponse);
}
