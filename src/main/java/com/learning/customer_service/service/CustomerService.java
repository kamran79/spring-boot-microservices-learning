
package com.learning.customer_service.service;

import com.learning.customer_service.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomerService {

    private final List<Customer> customers = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(3);

    public CustomerService() {
        customers.add(new Customer(1L, "Ali", "ali@example.com"));
        customers.add(new Customer(2L, "Sara", "sara@example.com"));
    }


    public synchronized List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public synchronized Customer getCustomerById(Long id) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    public synchronized int getCustomersSize() {
        return customers.size();
    }

    public synchronized Customer createCustomer(Customer customer) {
        Customer newCustomer = new Customer(
                nextId.getAndIncrement(),
                customer.getName().trim(),
                customer.getEmail().trim()
        );

        customers.add(newCustomer);
        return newCustomer;
    }

    // PUT update customer
    public Customer updateCustomer(Long id, Customer customer) {

        return customers.stream()
                .filter(customerLocal -> customerLocal.getId().equals(id))
                .findFirst()
                .map(existingCustomer -> {
                    existingCustomer.setName(customer.getName());
                    existingCustomer.setEmail(customer.getEmail());
                    return existingCustomer;
                })
                .orElse(null);
    }

    private Customer throwNotFound(Long id) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer not found: " + id
        );
    }
    public synchronized boolean deleteCustomer(Long id) {

        customers.removeIf(
                customerLocal -> customerLocal.getId().equals(id)
        );
        return false;
    }
}
