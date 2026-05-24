package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private Long id;
    private String userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
