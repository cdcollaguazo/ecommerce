package com.cdcollaguazo.ecommerce.inventory_service.event;

public record OrderCancelledEvent(
        String orderNumber,
        String email,
        String reason
) {
}
