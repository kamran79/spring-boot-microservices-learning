
package com.learning.customer_service.service;

import com.learning.customer_service.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;

@Service
public class CustomerService {

    private final Map<Long, Customer> customers =
            new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(2);

    public CustomerService() {
        customers.put(1L,
                new Customer(1L, "Ali", "ali@example.com"));
        customers.put(2L,
                new Customer(2L, "Sara", "sara@example.com"));
    }

    // GET all customers
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    // GET customer by ID
    public Customer getCustomerById(Long id) {
        Customer customer = customers.get(id);

        if (customer == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found: " + id
            );
        }

        return customer;
    }

    // GET total count
    public int getCustomersSize() {
        return customers.size();
    }

    // POST create customer
    public Customer createCustomer(Customer customer) {

        Long newId = idGenerator.incrementAndGet();

        Customer newCustomer = new Customer(
                newId,
                customer.getName(),
                customer.getEmail()
        );

        customers.put(newId, newCustomer);

        return newCustomer;
    }

    // PUT update customer
    public Customer updateCustomer(Long id, Customer customer) {

        return customers.computeIfPresent(id, (key, existing) ->
                new Customer(id, customer.getName(),
                        customer.getEmail())
        ) != null ? getCustomerById(id) : throwNotFound(id);
    }

    private Customer throwNotFound(Long id) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Customer not found: " + id
        );
    }

    // DELETE customer
    public void deleteCustomer(Long id) {

        Customer removed = customers.remove(id);

        if (removed == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found: " + id
            );
        }
    }
}
