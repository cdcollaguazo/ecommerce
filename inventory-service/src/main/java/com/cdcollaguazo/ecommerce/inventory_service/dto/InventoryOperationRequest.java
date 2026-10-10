package com.cdcollaguazo.ecommerce.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReduceInventoryQuantity.class, name = "REDUCE_QUANTITY")
})
public interface InventoryOperationRequest {
}
