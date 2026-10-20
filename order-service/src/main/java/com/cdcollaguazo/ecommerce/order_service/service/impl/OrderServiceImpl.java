package com.cdcollaguazo.ecommerce.order_service.service.impl;

import com.cdcollaguazo.ecommerce.order_service.dto.*;
import com.cdcollaguazo.ecommerce.order_service.entity.Order;
import com.cdcollaguazo.ecommerce.order_service.entity.OrderLineItem;
import com.cdcollaguazo.ecommerce.order_service.entity.OrderStatus;
import com.cdcollaguazo.ecommerce.order_service.event.OrderPlacedEvent;
import com.cdcollaguazo.ecommerce.order_service.exception.OrderNotFoundException;
import com.cdcollaguazo.ecommerce.order_service.mapper.OrderMapper;
import com.cdcollaguazo.ecommerce.order_service.repository.OrderRepository;
import com.cdcollaguazo.ecommerce.order_service.service.OrderService;
import com.cdcollaguazo.ecommerce.order_service.service.client.InventoryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RefreshScope
public class OrderServiceImpl implements OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final boolean isOrderEnabled;
    private final RabbitTemplate rabbitTemplate;

    public OrderServiceImpl(OrderRepository orderRepository, InventoryClient inventoryClient,
                            @Value("${order.enabled}") boolean isOrderEnabled, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.isOrderEnabled = isOrderEnabled;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public OrderResponse createOrder(OrderRequest request, String userId) {
        if (!isOrderEnabled) {
            throw new RuntimeException("Orders are currently disabled due to maintenance");
        }

        Order order = OrderMapper.toOrder(request);
        order.setUserId(userId);

        /*
        for (OrderLineItem item : order.getOrderLineItems()) {
            inventoryClient.reduceInventoryQuantityRequest(item.getSku(),
                    new ReduceInventoryQuantityRequest("REDUCE_QUANTITY", item.getQuantity()));
        }
        */

        order.setOrderNumber(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.PLACED);

        Order savedOrder = orderRepository.save(order);
        log.info("Order created {}", savedOrder.getId());

        List<OrderPlacedEvent.OrderItemEvent> orderItems = order.getOrderLineItems().stream()
                .map(item -> new OrderPlacedEvent.OrderItemEvent(item.getSku(), item.getPrice().toString(), item.getQuantity()))
                .toList();

        OrderPlacedEvent event = new OrderPlacedEvent(savedOrder.getOrderNumber(), request.email(), orderItems);

        rabbitTemplate.convertAndSend("order-events", "order.placed", event);
        log.info("Order placed event sent {}", event);

        return OrderMapper.toOrderResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getOrders(String userId, boolean isAdmin) {
        List<Order> orders;

        if (isAdmin) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findByUserId(userId);
        }

        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(OrderMapper::toOrderResponse)
                .orElseThrow(() -> new OrderNotFoundException("id", id));
    }

    @Override
    public void deleteOrderById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("id", id);
        }

        orderRepository.deleteById(id);
        log.info("Order with id {} was deleted", id);
    }

    @Override
    public void updateOrderStatus(String orderNumber, OrderStatus newStatus) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException("orderNumber", orderNumber));

        order.setStatus(newStatus);

        orderRepository.save(order);
    }

}
