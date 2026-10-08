package com.cdcollaguazo.ecommerce.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "must not be blank")
        String name,
        String description,
        @NotNull(message = "must not be null")
        @Positive(message = "must be positive")
        BigDecimal price
) {
}
