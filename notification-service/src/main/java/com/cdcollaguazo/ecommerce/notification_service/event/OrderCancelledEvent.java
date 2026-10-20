package com.cdcollaguazo.ecommerce.notification_service.event;

public record OrderCancelledEvent(
        String orderNumber,
        String email,
        String reason
) {
}
