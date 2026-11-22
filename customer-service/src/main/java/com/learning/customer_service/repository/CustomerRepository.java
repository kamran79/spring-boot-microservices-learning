package com.learning.customer_service.repository;

import com.learning.customer_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    Optional<Customer> findByEmail(String email);
    Page<Customer> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable);

    Page<Customer> findByEmailContainingIgnoreCase(
            String email,
            Pageable pageable);
}