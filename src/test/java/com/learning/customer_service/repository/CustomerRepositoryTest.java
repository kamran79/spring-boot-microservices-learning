package com.learning.customer_service.repository;

import com.learning.customer_service.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        //   Arrange
        Customer customer = new Customer(null,"kamran","kamran@gmail.com");
        //   Act
        Customer savedCustomer = customerRepository.save(customer);
        //   Assert
        assertNotNull(savedCustomer.getId());
        assertEquals("kamran",savedCustomer.getName());

    }

    @Test
    void testUpdateCustomer() {
        //   Arrange
        Customer customer = new Customer(null,"kamran","kamran@gmail.com");
        Customer savedCustomer = customerRepository.save(customer);

        //   Act
        savedCustomer.setName("kamranupd");
        savedCustomer.setEmail("kamranupd@gmail.com");
        customerRepository.save(savedCustomer);

        //   Assert
        Customer updatedCustomer = customerRepository
                .findById(savedCustomer.getId())
                .orElseThrow();

        assertEquals("kamranupd", updatedCustomer.getName());
        assertEquals("kamranupd@gmail.com", updatedCustomer.getEmail());

    }

}
