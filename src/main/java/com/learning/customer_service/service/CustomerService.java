
package com.learning.customer_service.service;

import com.learning.customer_service.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    public List<Customer> getAllCustomers() {
        return List.of(
            new Customer(1L, "Ali", "ali@example.com"),
            new Customer(2L, "Sara", "sara@example.com")
        );
    }


    public int getCustomersSize() {
       return getAllCustomers().size();
    }
}
