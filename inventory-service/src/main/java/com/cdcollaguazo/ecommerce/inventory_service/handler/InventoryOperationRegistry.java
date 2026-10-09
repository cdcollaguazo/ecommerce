package com.cdcollaguazo.ecommerce.inventory_service.handler;

import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryOperationRequest;
import com.cdcollaguazo.ecommerce.inventory_service.execption.InvalidInventoryOperationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryOperationRegistry {

    private final List<InventoryOperation> inventoryOperations;

    public InventoryOperationRegistry(List<InventoryOperation> inventoryOperations) {
        this.inventoryOperations = inventoryOperations;
    }

    public InventoryOperation getHandler(InventoryOperationRequest request) {
        return inventoryOperations.stream()
                .filter(operation -> operation.supports(request))
                .findAny()
                .orElseThrow(() -> new InvalidInventoryOperationException("Invalid operation"));

    }

}
