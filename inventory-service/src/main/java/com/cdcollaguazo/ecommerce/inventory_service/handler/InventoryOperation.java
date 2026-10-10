package com.cdcollaguazo.ecommerce.inventory_service.handler;

import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryOperationRequest;
import com.cdcollaguazo.ecommerce.inventory_service.entity.Inventory;

public interface InventoryOperation {

    boolean supports(Class<?> request);
    Inventory apply(Inventory inventory, InventoryOperationRequest request);

}
