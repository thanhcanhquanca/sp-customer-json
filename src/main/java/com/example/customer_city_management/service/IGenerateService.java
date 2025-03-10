package com.example.customer_city_management.service;


import java.util.Optional;

public interface IGenerateService<T> {
    Iterable<T> getAll();
    Optional<T> findById(Long id);
    void save(T t);
    void delete(Long id);

}
