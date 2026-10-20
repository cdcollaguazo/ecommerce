package com.cdcollaguazo.ecommerce.order_service.listener;

import com.cdcollaguazo.ecommerce.order_service.entity.OrderStatus;
import com.cdcollaguazo.ecommerce.order_service.event.OrderCancelledEvent;
import com.cdcollaguazo.ecommerce.order_service.event.OrderConfirmedEvent;
import com.cdcollaguazo.ecommerce.order_service.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final Logger log = LoggerFactory.getLogger(OrderEventListener.class);
    private final OrderService orderService;

    public OrderEventListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "order-confirmed-queue")
    public void handleOrderConfirmedEvent(OrderConfirmedEvent event) {
        orderService.updateOrderStatus(event.orderNumber(), OrderStatus.CONFIRMED);
    }

    @RabbitListener(queues = "order-cancelled-queue")
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {
        orderService.updateOrderStatus(event.orderNumber(), OrderStatus.CANCELLED);
    }

}
