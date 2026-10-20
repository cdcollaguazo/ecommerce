package com.cdcollaguazo.ecommerce.inventory_service.event;

public record OrderConfirmedEvent(
        String orderNumber,
        String email
) {
}
