package com.ecommerce.order.service;

import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.CartItemResponse;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.exception.NotEnoughQuantityInStockException;
import com.ecommerce.order.exception.NotFoundException;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService{

    private final CartItemRepository cartItemRepo;

    private final ProductServiceClient productServiceClient;

    private final UserServiceClient userServiceClient;

    public CartItemServiceImpl(CartItemRepository cartItemRepo, ProductServiceClient productServiceClient, UserServiceClient userServiceClient){
        this.cartItemRepo = cartItemRepo;
        this.productServiceClient = productServiceClient;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void addToCart(String userId, CartItemRequest cartItemRequest) {

        ProductResponse productResponse = productServiceClient.getProductDetails(String.valueOf(cartItemRequest.getProductId()));
        if(productResponse.getId()==null){throw new NotFoundException("Product not found with Id: " + cartItemRequest.getProductId());}

        if(productResponse.getStockQuantity()<cartItemRequest.getQuantity()){throw new NotEnoughQuantityInStockException("Not enough quantity in stock");}

        UserResponse user = userServiceClient.getUserDetails(userId);
        if(user.getId()==null){throw new NotFoundException("User not found with Id: " + userId);}

        CartItem existingCartItem = cartItemRepo.findByUserIdAndProductId(userId,cartItemRequest.getProductId());
        if(existingCartItem!=null){
            //Update the quantity for existing product
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            //existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepo.saveAndFlush(existingCartItem);
        }else{
            //Add item to the cart
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(cartItemRequest.getProductId());
            cartItem.setQuantity(cartItemRequest.getQuantity());
            //cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepo.saveAndFlush(cartItem);
        }
    }

    @Override
    public void deleteCart(String userId, Long productId) {
        UserResponse user = userServiceClient.getUserDetails(userId);
        if(user.getId()==null){throw new NotFoundException("User not found with Id: " + userId);}

        ProductResponse productResponse = productServiceClient.getProductDetails(String.valueOf(productId));
        if(productResponse.getId()==null){throw new NotFoundException("Product not found with Id: " + productId);}

        CartItem existingCartItem = cartItemRepo.findByUserIdAndProductId(userId,productId);
        if(existingCartItem!=null){
            cartItemRepo.delete(existingCartItem);
        }else{
            throw new NotFoundException("Cart Item not found for userID : " + userId + " and productID: " + productId);
        }

    }

    @Override
    public List<CartItemResponse> getAllCartItem(String userId) {
        UserResponse user = userServiceClient.getUserDetails(String.valueOf(userId));
        if(user.getId()==null){throw new NotFoundException("User not found with Id: " + userId);}

        return cartItemRepo.findByUserId(userId).stream().map(cartItem -> new CartItemResponse(cartItem.getId(), cartItem.getUserId(),cartItem.getProductId(),cartItem.getQuantity(),cartItem.getPrice(),cartItem.getCreatedAt(),cartItem.getUpdatedAt())).toList();
    }

    @Override
    public List<CartItem> getCartItems(String userId) {
        UserResponse user = userServiceClient.getUserDetails(String.valueOf(userId));
        if(user.getId()==null){throw new NotFoundException("User not found with Id: " + userId);}

        return cartItemRepo.findByUserId(userId).stream().map(cartItem -> new CartItem(cartItem.getId(), cartItem.getUserId(),cartItem.getProductId(),cartItem.getQuantity(),cartItem.getPrice(),cartItem.getCreatedAt(),cartItem.getUpdatedAt())).toList();
    }


    @Override
    public void clear(String userId) {
        //userRepo.findById(Long.valueOf(userId)).ifPresent();
        cartItemRepo.deleteByUserId(userId);
    }
}
