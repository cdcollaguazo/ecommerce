package com.cdcollaguazo.ecommerce.order_service.schedluer;

import com.cdcollaguazo.ecommerce.order_service.entity.OutboxEvent;
import com.cdcollaguazo.ecommerce.order_service.event.OrderPlacedEvent;
import com.cdcollaguazo.ecommerce.order_service.service.OutboxEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class MessageRelayer {

    private final Logger log = LoggerFactory.getLogger(MessageRelayer.class);
    private final RabbitTemplate rabbitTemplate;
    private final OutboxEventService outboxEventService;
    private final ObjectMapper objectMapper;

    public MessageRelayer(RabbitTemplate rabbitTemplate, OutboxEventService outboxEventService, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.outboxEventService = outboxEventService;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 10000)
    public void relayMessage() {
        List<OutboxEvent> pendingEvents = outboxEventService.getPendingEvents();
        log.info("Found {} pending events", pendingEvents.size());

        for (OutboxEvent event : pendingEvents) {
            try {
                OrderPlacedEvent originalEvent = objectMapper.readValue(event.getPayload(), OrderPlacedEvent.class);

                rabbitTemplate.convertAndSend("order-events", "order.placed", originalEvent);

                outboxEventService.markEventAsProcessed(event.getId());

                log.info("Event {} processed successfully", event.getAggregateId());
            } catch (AmqpException e) {
                log.warn("Error while sending event {}", event.getAggregateId(), e);
            }
        }

    }

}
