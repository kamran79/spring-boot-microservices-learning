package com.learning.customer_service.mapper;

import com.learning.customer_service.dto.CustomerRequest;
import com.learning.customer_service.dto.CustomerResponse;
import com.learning.customer_service.model.Customer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    private final CustomerMapper mapper =
            Mappers.getMapper(CustomerMapper.class);

    @Test
    void testToEntity() {

        // Arrange
        CustomerRequest request =
                new CustomerRequest(
                        "Ahmed",
                        "ahmed@example.com"
                );

        // Act
        Customer customer = mapper.toEntity(request);

        // Assert
        assertNull(customer.getId());
        assertEquals("Ahmed", customer.getName());
        assertEquals("ahmed@example.com", customer.getEmail());
    }

    @Test
    void testToResponse() {

        // Arrange
        Customer customer =
                new Customer(
                        10L,
                        "Sara",
                        "sara@example.com"
                );

        // Act
        CustomerResponse response = mapper.toResponse(customer);

        // Assert
        assertEquals(10L, response.id());
        assertEquals("Sara", response.name());
        assertEquals("sara@example.com", response.email());
    }
}