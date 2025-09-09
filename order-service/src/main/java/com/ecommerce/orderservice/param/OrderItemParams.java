package com.ecommerce.orderservice.param;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemParams {

    long productId;
    @Min(1)
    int quantity;

}
