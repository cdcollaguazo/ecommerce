package com.cdcollaguazo.ecommerce.order_service.service.impl;

import com.cdcollaguazo.ecommerce.order_service.dto.*;
import com.cdcollaguazo.ecommerce.order_service.entity.Order;
import com.cdcollaguazo.ecommerce.order_service.entity.OrderLineItem;
import com.cdcollaguazo.ecommerce.order_service.exception.OrderNotFoundException;
import com.cdcollaguazo.ecommerce.order_service.mapper.OrderMapper;
import com.cdcollaguazo.ecommerce.order_service.repository.OrderRepository;
import com.cdcollaguazo.ecommerce.order_service.service.OrderService;
import com.cdcollaguazo.ecommerce.order_service.service.client.InventoryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public OrderServiceImpl(OrderRepository orderRepository, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Order order = OrderMapper.toOrder(request);

        for (OrderLineItem item : order.getOrderLineItems()) {
            String sku = item.getSku();
            ReduceInventoryQuantity operation = new ReduceInventoryQuantity(item.getQuantity());

            inventoryClient.runInventoryOperation(sku, new InventoryOperationRequest(operation));
        }

        order.setOrderNumber(UUID.randomUUID().toString());

        Order savedOrder = orderRepository.save(order);
        log.info("Order created {}", savedOrder.getId());

        return OrderMapper.toOrderResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(OrderMapper::toOrderResponse)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public void deleteOrderById(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }

        orderRepository.deleteById(id);
        log.info("Order with id {} was deleted", id);
    }

}
