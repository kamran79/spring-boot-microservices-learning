package com.learning.customer_service.repository;

import com.learning.customer_service.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CustomerPaginationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();

        customerRepository.save(
                new Customer(null, "Kamran", "kamran@test.com"));

        customerRepository.save(
                new Customer(null, "Ahmed", "ahmed@test.com"));

        customerRepository.save(
                new Customer(null, "John", "john@test.com"));

        customerRepository.save(
                new Customer(null, "Ali", "ali@test.com"));

        customerRepository.save(
                new Customer(null, "Sara", "sara@test.com"));
    }

    @Test
    void testPagination() {

        Pageable pageable = PageRequest.of(0, 2);

        Page<Customer> result =
                customerRepository.findAll(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(5, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
    }
    @Test
    void testSortingByNameAscending() {

        Pageable pageable = PageRequest.of(
                0,
                5,
                Sort.by("name").ascending()
        );

        Page<Customer> result =
                customerRepository.findAll(pageable);
        assertEquals(5, result.getTotalElements());
        assertEquals("Ahmed",result.getContent().get(0).getName());
        assertEquals("Ali",result.getContent().get(1).getName());
        assertEquals("John",result.getContent().get(2).getName());
    }

    @Test
    void testSearchByNameIgnoreCase() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Customer> result =
                customerRepository.findByNameContainingIgnoreCase(
                        "KAM",
                        pageable
                );

        assertEquals(1, result.getTotalElements());
        assertEquals("Kamran",
                result.getContent().get(0).getName());
    }
}
