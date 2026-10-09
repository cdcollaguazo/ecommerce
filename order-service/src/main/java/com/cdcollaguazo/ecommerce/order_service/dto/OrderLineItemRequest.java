package com.cdcollaguazo.ecommerce.order_service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record OrderLineItemRequest(
        @NotBlank(message = "must not be blank")
        String sku,
        @NotNull(message = "must not be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "must be bigger than 0.0")
        BigDecimal price,
        @NotNull(message = "must not be null")
        @Min(value = 1, message = "must be bigger than 1")
        Integer quantity
) {
}
