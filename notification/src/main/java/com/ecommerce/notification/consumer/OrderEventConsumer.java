package com.ecommerce.notification.consumer;

import com.ecommerce.notification.dto.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderEventConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(OrderCreatedEvent orderCreatedEvent){
        System.out.println("Received Status: " + orderCreatedEvent.toString());

    }
}
