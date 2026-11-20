package com.learning.customer_service.dto;

public record LoginRequest(
        String username,
        String password
) {}