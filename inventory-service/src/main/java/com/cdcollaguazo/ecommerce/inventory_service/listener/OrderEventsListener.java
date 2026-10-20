package com.cdcollaguazo.ecommerce.inventory_service.listener;

import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryOperationRequest;
import com.cdcollaguazo.ecommerce.inventory_service.dto.ReduceInventoryQuantity;
import com.cdcollaguazo.ecommerce.inventory_service.event.OrderPlacedEvent;
import com.cdcollaguazo.ecommerce.inventory_service.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderEventsListener {

    private final InventoryService inventoryService;
    private final Logger log = LoggerFactory.getLogger(OrderPlacedEvent.class);

    public OrderEventsListener(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RabbitListener(queues = "inventory-queue")
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        try {
            List<Pair<String, InventoryOperationRequest>> operations = event.items().stream()
                    .map(item -> Pair.of(item.sku(), (InventoryOperationRequest)new ReduceInventoryQuantity(item.quantity())))
                    .toList();

            inventoryService.applyInventoryOperations(operations);
        } catch (Exception e) {
            log.warn("Error while processing event {}", event, e);
        }
    }

}
