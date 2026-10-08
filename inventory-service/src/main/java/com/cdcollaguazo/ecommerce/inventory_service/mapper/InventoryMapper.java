package com.cdcollaguazo.ecommerce.inventory_service.mapper;

import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryRequest;
import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryResponse;
import com.cdcollaguazo.ecommerce.inventory_service.entity.Inventory;

public class InventoryMapper {

    public static Inventory toInventory(InventoryRequest inventoryRequest) {
        return Inventory.builder()
                .sku(inventoryRequest.sku())
                .quantity(inventoryRequest.quantity())
                .build();
    }

    public static InventoryResponse toInventoryResponse(Inventory inventory) {
        return new InventoryResponse(inventory.getId(), inventory.getSku(), inventory.getQuantity(),
                inventory.getQuantity() > 0);
    }

}
