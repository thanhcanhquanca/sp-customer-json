package com.example.customer_city_management.service;

import com.example.customer_city_management.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomerService extends IGenerateService<Customer> {
    Page<Customer> findAllBy(Pageable pageable);

    Page<Customer> searchByName(String name, Pageable pageable);
}
