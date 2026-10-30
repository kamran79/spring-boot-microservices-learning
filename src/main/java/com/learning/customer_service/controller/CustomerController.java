
package com.learning.customer_service.controller;

import com.learning.customer_service.model.Customer;
import com.learning.customer_service.service.CustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Get all customers
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // Get total customer count
    @GetMapping("/count")
    public int getCustomerCount() {
        return customerService.getAllCustomers().size();
    }

    // Get customer by ID
    @GetMapping("/{customerId}")
    public Customer getCustomer(@PathVariable Long customerId) {

        return customerService.getAllCustomers()
                .stream()
                .filter(customer ->
                        customer.getId().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found with ID: " + customerId
                ));
    }
}
