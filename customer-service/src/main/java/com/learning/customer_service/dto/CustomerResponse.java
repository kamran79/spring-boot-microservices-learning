package com.learning.customer_service.dto;

public record CustomerResponse(
        Long id,
        String name,
        String email
) {
}