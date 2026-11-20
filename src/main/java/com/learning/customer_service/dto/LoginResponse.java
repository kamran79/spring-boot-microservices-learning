package com.learning.customer_service.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {}