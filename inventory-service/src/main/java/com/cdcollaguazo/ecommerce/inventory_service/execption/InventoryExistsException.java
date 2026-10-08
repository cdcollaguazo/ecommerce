package com.cdcollaguazo.ecommerce.inventory_service.execption;

public class InventoryExistsException extends RuntimeException {

    public InventoryExistsException(String field, Object value) {
        super("Inventory with " + field +  ": " + value + " already exists");
    }

}
