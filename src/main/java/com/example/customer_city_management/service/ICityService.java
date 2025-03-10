package com.example.customer_city_management.service;

import com.example.customer_city_management.model.City;

import java.util.List;

public interface ICityService extends IGenerateService<City> {
    List<City> findAll();
}
