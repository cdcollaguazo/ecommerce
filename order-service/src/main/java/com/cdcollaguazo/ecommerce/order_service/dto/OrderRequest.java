package com.cdcollaguazo.ecommerce.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequest(
        @NotEmpty(message = "must not be empty")
        @Valid
        List<OrderLineItemRequest> orderLineItemRequests
) {
}
