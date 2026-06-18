package com.ecommerce.order.dto;

import com.ecommerce.order.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private String userId;
    private OrderStatus status;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updateTime;


    public OrderCreatedEvent(Long id, String userId, OrderStatus orderStatus,
                             List<OrderItemResponse> orderItemResponses,
                             BigDecimal totalAmount, LocalDateTime createdAt,
                             LocalDateTime updatedAt) {
        this.orderId = id;
        this.userId = userId;
        this.status = orderStatus;
        this.items = orderItemResponses;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updateTime = updatedAt;
    }
}
