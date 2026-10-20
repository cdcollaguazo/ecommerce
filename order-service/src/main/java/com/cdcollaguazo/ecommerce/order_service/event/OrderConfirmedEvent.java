package com.cdcollaguazo.ecommerce.order_service.event;

public record OrderConfirmedEvent(
        String orderNumber,
        String email
) {
}
