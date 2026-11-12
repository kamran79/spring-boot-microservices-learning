
package com.learning.customer_service.service;

import com.learning.customer_service.model.Customer;
import com.learning.customer_service.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // GET all customers
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    // GET customer by ID
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    // GET customer by email
    @Transactional(readOnly = true)
    public Customer findByEmail(String email) {

        Optional<Customer> result =
                customerRepository.findByEmail(email);

        return result.orElse(null);
    }


    // Count customers
    @Transactional(readOnly = true)
    public long getCustomersSize() {
        return customerRepository.count();
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // PUT update customer
    @Transactional
    public Customer updateCustomer(Long id, Customer customer) {

        return customerRepository.findById(id)
                .map(existingCustomer->{
                    existingCustomer.setName(customer.getName());
                    existingCustomer.setEmail(customer.getEmail());
                    return customerRepository.save(existingCustomer);
                }).orElse(null);
    }

    private Customer throwNotFound(Long id) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer not found: " + id
        );
    }
    @Transactional
    public boolean deleteCustomer(Long id) {

        if (!customerRepository.existsById(id)) {
            return false;
        }

        customerRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void demonstrateRollback(Customer customer) {

        // Step 1: Insert customer
        customerRepository.saveAndFlush(customer);

        // Step 2: Simulate a business failure
        throw new IllegalStateException(
                "Simulated failure - rollback transaction"
        );
    }
    @Transactional
    public void createCustomerAndFail() {
        Customer customer = new Customer(
                null,
                "Kamran",
                "kamran@example.com"
        );

        customerRepository.save(customer);

        throw new RuntimeException("Simulated failure");
    }
}
