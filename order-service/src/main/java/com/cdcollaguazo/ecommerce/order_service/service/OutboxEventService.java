package com.cdcollaguazo.ecommerce.order_service.service;

import com.cdcollaguazo.ecommerce.order_service.entity.OutboxEvent;
import com.cdcollaguazo.ecommerce.order_service.event.OrderPlacedEvent;

import java.util.List;

public interface OutboxEventService {

    void saveOrderPlacedEvent(OrderPlacedEvent event);
    List<OutboxEvent> getPendingEvents();
    void markEventAsProcessed(Long id);

}
