package com.ecommerce.order.service;

import com.ecommerce.order.dto.CartItemResponse;
import com.ecommerce.order.dto.OrderItemResponse;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.exception.NotFoundException;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;

    public OrderServiceImpl(OrderRepository orderRepository, CartItemService cartItemService){
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
    }

    @Override
    public OrderResponse createOrder(String userId) {

        // Validate for user
//        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty()){throw new NotFoundException("User not found with Id: "+userId);}
//        User user = userOpt.get();

        // Validate for cart items
        List<CartItem> cartItemList = cartItemService.getCartItems(userId);
        if(cartItemList.isEmpty()){throw new NotFoundException("There are no cart items available for userId: " + userId);}

        // Calculate total price
        BigDecimal totalPrice = cartItemList.stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO,BigDecimal::add);

        // Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItemList = cartItemList.stream().map(cartItem -> new OrderItem(cartItem.getProductId(),cartItem.getQuantity(),cartItem.getPrice(),order)).toList();
        order.setItems(orderItemList);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartItemService.clear(userId);

        return mapToOrderResponse(savedOrder);
    }

    private static OrderResponse mapToOrderResponse(Order order){
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getItems().stream().map(orderItem -> new OrderItemResponse(orderItem.getId(),Long.valueOf(orderItem.getProductId()),orderItem.getQuantity(),orderItem.getPrice(),orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))).toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
