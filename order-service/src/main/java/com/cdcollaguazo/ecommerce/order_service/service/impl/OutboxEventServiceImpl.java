package com.cdcollaguazo.ecommerce.order_service.service.impl;

import com.cdcollaguazo.ecommerce.order_service.entity.EventType;
import com.cdcollaguazo.ecommerce.order_service.entity.OutboxEvent;
import com.cdcollaguazo.ecommerce.order_service.event.OrderPlacedEvent;
import com.cdcollaguazo.ecommerce.order_service.exception.EventNotFoundException;
import com.cdcollaguazo.ecommerce.order_service.repository.OutboxEventRepository;
import com.cdcollaguazo.ecommerce.order_service.service.OutboxEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OutboxEventServiceImpl implements OutboxEventService {

    private final Logger log = LoggerFactory.getLogger(OutboxEventServiceImpl.class);
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public OutboxEventServiceImpl(OutboxEventRepository outboxEventRepository, ObjectMapper objectMapper) {
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveOrderPlacedEvent(OrderPlacedEvent event) {
        String payload = objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateId(event.orderNumber())
                .type(EventType.ORDER_PLACED)
                .payload(payload)
                .createdAt(LocalDateTime.now())
                .isProcessed(false)
                .build();

        outboxEventRepository.save(outboxEvent);
        log.info("OrderPlacedEvent saved correctly in outbox: {}", event.orderNumber());
    }

    @Override
    public List<OutboxEvent> getPendingEvents() {
        return outboxEventRepository.findByIsProcessedFalse();
    }

    @Override
    public void markEventAsProcessed(Long id) {
        OutboxEvent event = outboxEventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        event.setProcessed(true);

        outboxEventRepository.save(event);
    }

}
