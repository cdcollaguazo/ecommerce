package com.cdcollaguazo.ecommerce.order_service.exception;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(Long id) {
        super("Event with id " + id + "was not found");
    }

}
