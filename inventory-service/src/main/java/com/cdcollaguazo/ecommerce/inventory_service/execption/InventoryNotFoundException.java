package com.cdcollaguazo.ecommerce.inventory_service.execption;

public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(String field, Object value) {
        super("Inventory with " + field + ": " + value + " was not found");
    }

}
