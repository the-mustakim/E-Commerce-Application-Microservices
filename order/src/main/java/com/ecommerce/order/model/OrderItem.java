package com.ecommerce.order.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Integer quantity;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "id")
    private Order order;

    public OrderItem(Long productId, Integer quantity, BigDecimal price, Order order) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.order = order;
    }
}
