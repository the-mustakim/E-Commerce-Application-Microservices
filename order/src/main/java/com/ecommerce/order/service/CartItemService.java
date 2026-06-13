package com.ecommerce.order.service;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.CartItemResponse;
import com.ecommerce.order.model.CartItem;

import java.util.List;

public interface CartItemService {
    void addToCart(String userId, CartItemRequest cartItemRequest);

    void deleteCart(String userId, Long productId);

    List<CartItemResponse> getAllCartItem(String userId);

    List<CartItem> getCartItems(String userId);

    void clear(String userId);
}
