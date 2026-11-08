
package com.learning.customer_service.controller;

import com.learning.customer_service.model.Customer;
import com.learning.customer_service.service.CustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.learning.customer_service.dto.CustomerRequest;
import jakarta.validation.Valid;

import java.util.List;
import com.learning.customer_service.dto.CustomerResponse;
import com.learning.customer_service.mapper.CustomerMapper;
import java.util.List;



@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerMapper customerMapper;

    private final CustomerService customerService;

    public CustomerController(
            CustomerService customerService,
            CustomerMapper customerMapper) {

        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    // Get total customer count
    @GetMapping("/count")
    public long getCustomerCount() {
        return customerService.getCustomersSize();
    }

    // Get customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @PathVariable Long id) {

        Customer customer = customerService.getCustomerById(id);

        if (customer == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found with ID: " + id
            );
        }

        return ResponseEntity.ok(
                customerMapper.toResponse(customer)
        );
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {

        List<CustomerResponse> responses =
                customerService.getAllCustomers()
                        .stream()
                        .map(customerMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {

        Customer customer = customerMapper.toEntity(request);

        Customer updatedCustomer =
                customerService.updateCustomer(id, customer);

        if (updatedCustomer == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found with ID: " + id
            );
        }

        return ResponseEntity.ok(
                customerMapper.toResponse(updatedCustomer)
        );
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRequest request) {

        Customer customer = customerMapper.toEntity(request);

        Customer createdCustomer =
                customerService.createCustomer(customer);

        CustomerResponse response =
                customerMapper.toResponse(createdCustomer);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
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
