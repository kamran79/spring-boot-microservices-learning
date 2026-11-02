
package com.learning.customer_service.controller;

import com.learning.customer_service.model.Customer;
import com.learning.customer_service.service.CustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.learning.customer_service.dto.CustomerRequest;
import jakarta.validation.Valid;

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
    /*@PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customer) {

        Customer updated =
                customerService.updateCustomer(id, customer);

        return ResponseEntity.ok(updated);
    }*/

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {

        Customer customer = new Customer(
                id,
                request.name(),
                request.email()
        );

        Customer updatedCustomer =
                customerService.updateCustomer(id, customer);

        if (updatedCustomer == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found with ID: " + id
            );
        }

        return ResponseEntity.ok(updatedCustomer);
    }


    /*@PostMapping
    public ResponseEntity<Customer> createCustomer(
            @RequestBody Customer customer) {

        Customer created =
                customerService.createCustomer( customer);

        return ResponseEntity.ok(created);
    }*/


    @PostMapping
    public ResponseEntity<Customer> createCustomer(
            @Valid @RequestBody CustomerRequest request) {

        Customer customer = new Customer(
                null,
                request.name(),
                request.email()
        );

        Customer createdCustomer =
                customerService.createCustomer(customer);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdCustomer);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {

        boolean deleted = customerService.deleteCustomer(id);

        if (!deleted) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found"
            );
        }

        return ResponseEntity.noContent().build();
    }

}
