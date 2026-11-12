package com.learning.customer_service.service;

import com.learning.customer_service.model.Customer;
import com.learning.customer_service.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CustomerTransactionRollbackTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testTransactionRollback() {

        // Arrange
        String email = "kamran@example.com";
        // Make sure the customer does not already exist
        customerRepository.findByEmail(email)
                .ifPresent(customerRepository::delete);
        // Act
        assertThrows(RuntimeException.class, () -> {
            // Call the method that throws the exception
            customerService.createCustomerAndFail();
        });
        // Hint 2: Call createCustomerAndFail().
        // Hint 3: Expect RuntimeException.

        // Assert
        // verify
        Optional<Customer> customer =
                customerRepository.findByEmail(email);
        assertTrue(customer.isEmpty(),"Customer should not exist after transaction rollback");
        // Hint 4: Query repository by email.
        // Hint 5: Verify customer does NOT exist.
    }
}