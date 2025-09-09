package com.ecommerce.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Table(name = "order_item")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemEntity extends BaseEntity {

    long productId;

    @Column(name = "product_name", nullable = false)
    String productName;

    @Column(nullable = false)
    int quantity;

    @Column(nullable = false)
    Double price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    OrderEntity order;
}
