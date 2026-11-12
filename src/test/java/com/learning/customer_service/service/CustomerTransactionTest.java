package com.learning.customer_service.service;

import com.learning.customer_service.model.Customer;
import com.learning.customer_service.repository.CustomerRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CustomerTransactionTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testTransactionRollback() {

        // Arrange
        String email = "rollback@example.com";

        Customer customer = new Customer(
                null,
                "Rollback Test",
                email
        );

        long countBefore = customerRepository.count();

        // Act + Assert: expect an exception
        assertThrows(
                IllegalStateException.class,
                () -> customerService.demonstrateRollback(customer)
        );

        // Verify no customer was committed
        assertTrue(
                customerRepository.findByEmail(email).isEmpty()
        );

        assertEquals(
                countBefore,
                customerRepository.count()
        );
    }

}