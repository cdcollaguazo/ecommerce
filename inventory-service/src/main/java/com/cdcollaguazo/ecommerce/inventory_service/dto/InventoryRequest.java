package com.cdcollaguazo.ecommerce.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryRequest(
        @NotNull
        String sku,
        @Min(value = 0)
        Integer quantity
) {
}
