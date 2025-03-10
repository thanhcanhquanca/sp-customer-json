package com.example.customer_city_management.repository;

import com.example.customer_city_management.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findAllBy(Pageable pageable);
    Page<Customer> findByFirstNameContaining(String firstName, Pageable pageable);
}
