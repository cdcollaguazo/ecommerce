package com.cdcollaguazo.ecommerce.notification_service.listener;

import com.cdcollaguazo.ecommerce.notification_service.event.OrderCancelledEvent;
import com.cdcollaguazo.ecommerce.notification_service.event.OrderConfirmedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "notification-queue")
public class OrderEventListener {

    private final Logger log = LoggerFactory.getLogger(OrderEventListener.class);
    private final JavaMailSender mailSender;

    public OrderEventListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitHandler
    public void handleOrderConfirmedEvent(OrderConfirmedEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@ecommerce.com");
        message.setTo(event.email());
        message.setSubject("Order confirmed - " + event.orderNumber());
        message.setText("""
            Hi,
            
            Your order with number %s has been processed successfully.
            Soon you will receive more news about the delivery.
            Thanks for trusting on us.
            
            
            Best regards,
            Ecommerce Team
            """.formatted(event.orderNumber()));

        mailSender.send(message);

        log.info("Email successfully sent to {}", event.email());
    }

    @RabbitHandler
    public void handleOrderCancelledEvent(OrderCancelledEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@ecommerce.com");
        message.setTo(event.email());
        message.setSubject("Order cancelled - " + event.orderNumber());
        message.setText("""
            Hi,
            
            Your order with number %s has been cancelled due to:
            %s.
    
            
            Best regards,
            Ecommerce Team
            """.formatted(event.orderNumber(), event.reason()));

        mailSender.send(message);

        log.info("Email successfully sent to {}", event.email());
    }

}
