package com.ecommerce.order.service;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.CartItemResponse;
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

    public CartItemServiceImpl(CartItemRepository cartItemRepo){
        this.cartItemRepo = cartItemRepo;
    }

    @Override
    public void addToCart(String userId, CartItemRequest cartItemRequest) {

//        Optional<Product> productOpt = productRepo.findById(cartItemRequest.getProductId());
//        if(productOpt.isEmpty()){throw new NotFoundException("Product not found with Id: " + cartItemRequest.getProductId());}
//
//        Product product = productOpt.get();
//        if(product.getStockQuantity()<cartItemRequest.getQuantity()){throw new NotEnoughQuantityInStockException("Not enough quantity in stock");}
//
//        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty()){throw new NotFoundException("User not found with Id: " + userId);}
//
//        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepo.findByUserIdAndProductId(Long.valueOf(userId),cartItemRequest.getProductId());
        if(existingCartItem!=null){
            //Update the quantity for existing product
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            //existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepo.saveAndFlush(existingCartItem);
        }else{
            //Add item to the cart
            CartItem cartItem = new CartItem();
            cartItem.setUserId(Long.valueOf(userId));
            cartItem.setProductId(cartItemRequest.getProductId());
            cartItem.setQuantity(cartItemRequest.getQuantity());
            //cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItemRequest.getQuantity())));
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepo.saveAndFlush(cartItem);
        }
    }

    @Override
    public void deleteCart(String userId, Long productId) {
//        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty()){throw new NotFoundException("User not found with Id: "+userId);}

//        Optional<Product> productOpt = productRepo.findById(productId);
//        if(productOpt.isEmpty()){throw new NotFoundException("Product not found with Id: " + productOpt);}

        CartItem existingCartItem = cartItemRepo.findByUserIdAndProductId(Long.valueOf(userId),productId);
        if(existingCartItem!=null){
            cartItemRepo.delete(existingCartItem);
        }else{
            throw new NotFoundException("Cart Item not found for userID : " + userId + " and productID: " + productId);
        }

    }

    @Override
    public List<CartItemResponse> getAllCartItem(Long userId) {
//        Optional<User> userOpt = userRepo.findById(userId);
//        if(userOpt.isEmpty()){throw new NotFoundException("User not found with Id: "+userId);}

        return cartItemRepo.findByUserId(userId).stream().map(cartItem -> new CartItemResponse(cartItem.getId(), cartItem.getUserId(),cartItem.getProductId(),cartItem.getQuantity(),cartItem.getPrice(),cartItem.getCreatedAt(),cartItem.getUpdatedAt())).toList();
    }

    public List<CartItem> getCartItems(Long userId) {
//        Optional<User> userOpt = userRepo.findById(userId);
//        if(userOpt.isEmpty()){throw new NotFoundException("User not found with Id: "+userId);}

        return cartItemRepo.findByUserId(userId).stream().map(cartItem -> new CartItem(cartItem.getId(), cartItem.getUserId(),cartItem.getProductId(),cartItem.getQuantity(),cartItem.getPrice(),cartItem.getCreatedAt(),cartItem.getUpdatedAt())).toList();
    }


    @Override
    public void clear(String userId) {
        //userRepo.findById(Long.valueOf(userId)).ifPresent();
        cartItemRepo.deleteByUserId(Long.valueOf(userId));
    }
}
