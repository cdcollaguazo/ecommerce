package com.cdcollaguazo.ecommerce.inventory_service.execption;

public class InvalidInventoryOperationException extends RuntimeException {

    public InvalidInventoryOperationException(String sku, String message) {
        super("Invalid operation for " + sku + ": " + message);
    }

    public InvalidInventoryOperationException(String message) {
        super(message);
    }

}
