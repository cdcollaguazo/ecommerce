package com.cdcollaguazo.ecommerce.order_service.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String field, Object value) {
        super("Order with " + field + ": " + value + " was not found");
    }

}
