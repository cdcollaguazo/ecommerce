package com.cdcollaguazo.ecommerce.inventory_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReduceInventoryQuantity(
        @NotNull(message = "must not be null")
        @Positive(message = "must be positive")
        Integer value
) {
}
