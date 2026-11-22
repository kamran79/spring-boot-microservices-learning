package com.learning.customer_service.service;

import com.learning.customer_service.model.Customer;
import com.learning.customer_service.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CustomerServiceUnitTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Kamran");
        customer.setEmail("kamran@example.com");
    }
    @Test
    void shouldReturnCustomerWhenIdExists() {

        // Arrange
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        // Act
        Customer result = customerService.getCustomerById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Kamran", result.getName());
        assertEquals("kamran@example.com", result.getEmail());

        verify(customerRepository, times(1))
                .findById(1L);
    }
    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {

        // Arrange
        when(customerRepository.findById(444L))
                .thenReturn(Optional.empty());

        // Act and Assert
        Customer result = customerService.getCustomerById(444L);
        assertNull(result);

        verify(customerRepository, times(1)).findById(444L);
    }
    @Test
    void shouldSaveCustomerSuccessfully() {

        // Arrange: Tell Mockito what to return when save() is called
        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer);

        // Act: Call the actual CustomerService method
        Customer result = customerService.createCustomer(customer);

        // Assert: Verify the returned customer
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Kamran", result.getName());
        assertEquals("kamran@example.com", result.getEmail());

        // Verify: Repository save() was called exactly once
        verify(customerRepository, times(1))
                .save(customer);
    }
    @Test
    void shouldGetCustomersPageSuccessfully() {

        // Arrange: Create pagination and sorting instructions
        Pageable pageable = PageRequest.of(
                0,
                3,
                Sort.by("name").ascending()
        );

        // Prepare a second customer
        Customer secondCustomer = new Customer();
        secondCustomer.setId(2L);
        secondCustomer.setName("Sara");
        secondCustomer.setEmail("sara@example.com");

        // Simulate a database page containing two customers
        Page<Customer> mockPage = new PageImpl<>(
                List.of(customer, secondCustomer),
                pageable,
                2
        );

        // Tell Mockito what to return
        when(customerRepository.findAll(pageable))
                .thenReturn(mockPage);


        // Act: Call the actual service method
        Page<Customer> result =
                customerService.getCustomersPage(pageable);

        // Assert: Verify pagination results
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getNumber());

        // Verify the repository interaction
        verify(customerRepository, times(1))
                .findAll(pageable);

    }

    @Test
    void shouldReturnSecondPageOfCustomers() {

        // Arrange: Request second page with 3 records
        Pageable pageable = PageRequest.of(1, 3);

        // Create only the customers returned on page 1
        Customer customer4 = createTestCustomer(
                4L, "Ali", "ali@example.com");

        Customer customer5 = createTestCustomer(
                5L, "Sara", "sara@example.com");

        Customer customer6 = createTestCustomer(
                6L, "John", "john@example.com");

        // Simulate a page with 3 customers
        // and 10 total customers in the database
        Page<Customer> mockPage = new PageImpl<>(
                List.of(customer4, customer5, customer6),
                pageable,
                10
        );

        // Mock the repository response
        when(customerRepository.findAll(pageable))
                .thenReturn(mockPage);

        // Act: Call the actual service
        Page<Customer> result =
                customerService.getCustomersPage(pageable);

        // Assert: Verify pagination information
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(1, result.getNumber());
        assertEquals(3, result.getSize());
        assertEquals(10, result.getTotalElements());
        assertEquals(4, result.getTotalPages());

        // Verify repository interaction
        verify(customerRepository, times(1))
                .findAll(pageable);
    }
    private Customer createTestCustomer(
            Long id,
            String name,
            String email) {

        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setEmail(email);

        return customer;
    }

}