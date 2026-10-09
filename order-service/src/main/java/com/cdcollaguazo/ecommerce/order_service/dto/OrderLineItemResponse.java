package com.cdcollaguazo.ecommerce.order_service.dto;

import java.math.BigDecimal;

public record OrderLineItemResponse(
        Long id,
        String sku,
        BigDecimal price,
        Integer quantity
) {
}
