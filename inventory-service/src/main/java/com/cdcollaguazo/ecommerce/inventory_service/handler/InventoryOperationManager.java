package com.cdcollaguazo.ecommerce.inventory_service.handler;

import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryOperationRequest;
import com.cdcollaguazo.ecommerce.inventory_service.entity.Inventory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryOperationManager {

    private final List<InventoryOperation> inventoryOperations;

    public InventoryOperationManager(List<InventoryOperation> inventoryOperations) {
        this.inventoryOperations = inventoryOperations;
    }

    public Inventory apply(Inventory inventory, InventoryOperationRequest request) {
        for (InventoryOperation operation : inventoryOperations) {
            if (!operation.supports(request.getClass())) {
                continue;
            }

            return operation.apply(inventory, request);
        }

        throw new IllegalStateException("Unsupported operation");
    }

}
