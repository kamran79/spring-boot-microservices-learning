package com.learning.order_service.model;

import java.math.BigDecimal;

public record Order(
        Long id,
        Long customerId,
        String productName,
        Integer quantity,
        BigDecimal price
) {
}