package com.cdcollaguazo.ecommerce.product_service.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super("Product with id " + message + " was not found");
    }

}
