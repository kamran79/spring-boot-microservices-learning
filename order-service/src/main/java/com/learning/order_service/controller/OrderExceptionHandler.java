package com.learning.order_service.controller;

import com.learning.order_service.service.OrderNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(
            OrderNotFoundException ex) {

        return Map.of("message", ex.getMessage());
    }
}