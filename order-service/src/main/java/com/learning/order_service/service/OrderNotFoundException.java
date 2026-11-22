package com.learning.order_service.service;

public class OrderNotFoundException
        extends RuntimeException {

    public OrderNotFoundException(Long id) {
        super("Order not found with ID: " + id);
    }
}