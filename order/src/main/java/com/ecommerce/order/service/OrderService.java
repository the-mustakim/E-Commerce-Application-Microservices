package com.ecommerce.order.service;


import com.ecommerce.order.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(String userId);
}
