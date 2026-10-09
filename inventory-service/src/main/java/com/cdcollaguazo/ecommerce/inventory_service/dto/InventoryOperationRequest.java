package com.cdcollaguazo.ecommerce.inventory_service.dto;

import jakarta.validation.Valid;

public record InventoryOperationRequest(
        @Valid
        ReduceInventoryQuantity reduceInventoryQuantity
) {
}
