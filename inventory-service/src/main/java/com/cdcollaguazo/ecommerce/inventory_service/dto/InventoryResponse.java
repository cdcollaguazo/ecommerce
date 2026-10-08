package com.cdcollaguazo.ecommerce.inventory_service.dto;

public record InventoryResponse(
        Long id,
        String sku,
        Integer quantity,
        boolean inStock
) {
}
