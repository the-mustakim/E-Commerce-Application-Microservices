package com.ecommerce.order.controller;


import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.CartItemResponse;
import com.ecommerce.order.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartItemService cartItemService;

    public CartController(CartItemService cartItemService){
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<Void> addToCart(
            @RequestHeader("X-USER-ID") String userId,
            @RequestBody CartItemRequest cartItemRequest){
        cartItemService.addToCart(userId, cartItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long productId){
        cartItemService.deleteCart(userId,productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItem(@RequestHeader("X-USER-ID") String userId){
        return ResponseEntity.status(HttpStatus.OK).body(cartItemService.getAllCartItem(userId));
    }

}
