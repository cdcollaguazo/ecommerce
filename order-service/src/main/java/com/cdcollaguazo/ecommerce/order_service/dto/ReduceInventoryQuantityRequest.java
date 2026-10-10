package com.cdcollaguazo.ecommerce.order_service.dto;

public record ReduceInventoryQuantityRequest(
        String type,
        Integer value
) {
}
