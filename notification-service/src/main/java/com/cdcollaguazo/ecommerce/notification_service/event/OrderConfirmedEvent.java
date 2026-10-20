package com.cdcollaguazo.ecommerce.notification_service.event;

public record OrderConfirmedEvent(
        String orderNumber,
        String email
) {
}
