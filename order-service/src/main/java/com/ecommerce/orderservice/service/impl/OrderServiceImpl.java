package com.ecommerce.orderservice.service.impl;

import com.ecommerce.orderservice.dto.OrderDto;
import com.ecommerce.orderservice.dto.UserDto;
import com.ecommerce.orderservice.entity.OrderEntity;
import com.ecommerce.orderservice.entity.OrderItemEntity;
import com.ecommerce.orderservice.enums.OrderStatus;
import com.ecommerce.orderservice.exception.InvalidOrderStatusException;
import com.ecommerce.orderservice.mapper.OrderMapper;
import com.ecommerce.orderservice.model.GetProductsResponse;
import com.ecommerce.orderservice.param.CreateOrderParams;
import com.ecommerce.orderservice.param.OrderItemParams;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.OrderService;
import com.ecommerce.orderservice.service.ProductProcessingService;
import com.ecommerce.orderservice.service.UserProcessingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;

    UserProcessingService userProcessingService;
    ProductProcessingService productProcessingService;

    @Override
    @Transactional
    public OrderDto createOrder(CreateOrderParams params, String username) {
        UserDto userDto = userProcessingService.fetchClientData(username);
        List<OrderItemEntity> itemEntities = fetchItems(params);

        OrderEntity orderEntity = createNewOrder(userDto, itemEntities);

        return orderMapper.toOrderDto(orderEntity, userDto);
    }

    @Override
    public void cancelOrder(long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order with id: '" + id + "' not found"));

        if (entity.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException("Order with id: '" + id +
                    "' with the status: '" + entity.getStatus() + "' cannot be canceled");
        }

        entity.setStatus(OrderStatus.CANCELED);
        orderRepository.save(entity);
        log.info("Order with id: {} has been successfully cancelled", id);
    }

    private List<OrderItemEntity> fetchItems(CreateOrderParams params) {
        Map<Long, Integer> productIdQuantityMap = new HashMap<>();
        List<Long> productIds = new ArrayList<>();

        for (OrderItemParams item : params.getItems()) {
            productIds.add(item.getProductId());
            productIdQuantityMap.merge(item.getProductId(), item.getQuantity(), Integer::sum);
        }
        GetProductsResponse productsResponse = productProcessingService.getProducts(productIds);

        return productsResponse.getProducts().stream()
                .map(product -> {
                    Integer quantity = productIdQuantityMap.get(product.getId());
                    return orderMapper.toOrderItemEntity(product, quantity);
                }).toList();
    }

    private OrderEntity createNewOrder(UserDto userDto, List<OrderItemEntity> itemEntities) {
        Double totalPrice = itemEntities.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        OrderEntity orderEntity = OrderEntity.builder()
                .userId(userDto.getId())
                .status(OrderStatus.PENDING)
                .totalPrice(totalPrice)
                .items(new ArrayList<>())
                .build();

        for (OrderItemEntity item : itemEntities) {
            item.setOrder(orderEntity);
            orderEntity.getItems().add(item);
        }

        orderEntity = orderRepository.save(orderEntity);
        log.info("Order created: {}", orderEntity);

        return orderEntity;
    }
}
