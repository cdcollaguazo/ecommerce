package com.cdcollaguazo.ecommerce.notification_service.listener;

import com.cdcollaguazo.ecommerce.notification_service.event.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsListener {

    private final Logger log = LoggerFactory.getLogger(OrderEventsListener.class);
    private final JavaMailSender mailSender;

    public OrderEventsListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = "notification-queue")
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@ecommerce.com");
            message.setTo(event.email());
            message.setSubject("Order confirmed - " + event.orderNumber());
            message.setText("""
                Hi!
                
                Your order with number %s has been processed successfully.
                Soon you will receive more news about the delivery.
                Thanks for trusting on us.
                
                
                Best regards,
                Ecommerce Team
                """.formatted(event.orderNumber()));

            mailSender.send(message);

            log.info("Email successfully sent to {}", event.email());
        } catch (Exception e) {
            log.warn("Error when sending email to {}", event.email(), e);
        }
    }

}
