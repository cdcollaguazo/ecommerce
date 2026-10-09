package com.cdcollaguazo.ecommerce.inventory_service.handler;

import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryOperationRequest;
import com.cdcollaguazo.ecommerce.inventory_service.entity.Inventory;

public interface InventoryOperation {

    boolean supports(InventoryOperationRequest request);
    void execute(Inventory inventory, InventoryOperationRequest request);

}
