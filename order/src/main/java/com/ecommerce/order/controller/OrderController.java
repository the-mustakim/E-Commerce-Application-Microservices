package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {


    private final OrderService orderService;

    public OrderController(OrderService orderService){this.orderService = orderService;}

    @PostMapping
    public ResponseEntity<OrderResponse> creatOrder(@RequestHeader("X-USER-ID") String userId){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(userId));
    }

}
