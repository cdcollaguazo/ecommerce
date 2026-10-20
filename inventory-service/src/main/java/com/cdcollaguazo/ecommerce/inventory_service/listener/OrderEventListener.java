package com.cdcollaguazo.ecommerce.inventory_service.listener;

import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryOperationRequest;
import com.cdcollaguazo.ecommerce.inventory_service.dto.ReduceInventoryQuantity;
import com.cdcollaguazo.ecommerce.inventory_service.event.OrderCancelledEvent;
import com.cdcollaguazo.ecommerce.inventory_service.event.OrderConfirmedEvent;
import com.cdcollaguazo.ecommerce.inventory_service.event.OrderPlacedEvent;
import com.cdcollaguazo.ecommerce.inventory_service.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderEventListener {

    private final Logger log = LoggerFactory.getLogger(OrderEventListener.class);
    private final InventoryService inventoryService;
    private final RabbitTemplate rabbitTemplate;

    public OrderEventListener(InventoryService inventoryService, RabbitTemplate rabbitTemplate) {
        this.inventoryService = inventoryService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "inventory-queue")
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        try {
            List<Pair<String, InventoryOperationRequest>> operations = event.items().stream()
                    .map(item -> Pair.of(item.sku(), (InventoryOperationRequest)new ReduceInventoryQuantity(item.quantity())))
                    .toList();

            inventoryService.applyInventoryOperations(operations);

            rabbitTemplate.convertAndSend("order-events", "order.confirmed", new OrderConfirmedEvent(event.orderNumber(), event.email()));
        } catch (Exception e) {
            log.warn("Error while processing event {}", event, e);

            OrderCancelledEvent cancelledEvent = new OrderCancelledEvent(
                    event.orderNumber(), event.email(), e.getMessage()
            );

            rabbitTemplate.convertAndSend("order-events", "order.cancelled", cancelledEvent);
        }
    }

}
