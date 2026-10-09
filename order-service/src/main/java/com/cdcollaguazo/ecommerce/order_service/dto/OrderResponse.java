package com.cdcollaguazo.ecommerce.order_service.dto;

import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        List<OrderLineItemResponse> orderLineItemResponses
) {
}
