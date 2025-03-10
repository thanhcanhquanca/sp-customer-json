package com.example.customer_city_management.controller;

import com.example.customer_city_management.dto.CustomerDTO;
import com.example.customer_city_management.model.City;
import com.example.customer_city_management.model.Customer;
import com.example.customer_city_management.service.CityService;
import com.example.customer_city_management.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CityService cityService;

    // Lấy tất cả khách hàng
    @GetMapping("/customers")
    public ResponseEntity<?> listAllCustomers(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size,
                                              @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Customer> customers = customerService.findAllBy(pageable);

        if (customers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<CustomerDTO> customerDTOS = customers.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(customerDTOS, HttpStatus.OK);
    }

    // Lấy khách hàng theo ID
    @GetMapping("/customers/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
        return new ResponseEntity<>(convertToDTO(customer.get()), HttpStatus.OK);
    }

    // Tạo khách hàng mới
    @PostMapping("/customers")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDTO customerDTO) {
        log.info("Creating customer with email: {}", customerDTO.getEmail());

        Optional<City> cityOptional = cityService.findById(customerDTO.getCityId());
        if (cityOptional.isEmpty()) {
            log.warn("Invalid cityId: {}", customerDTO.getCityId());
            return ResponseEntity.badRequest().body("Invalid cityId provided");
        }

        Customer customer = convertToEntity(customerDTO, cityOptional.get());
        customerService.save(customer);
        CustomerDTO createdDTO = convertToDTO(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDTO);
    }

    // Cập nhật khách hàng
    @PutMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        log.info("Updating customer with id: {}", id);

        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()) {
            log.warn("Customer not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        Optional<City> cityOptional = cityService.findById(customerDTO.getCityId());
        if (cityOptional.isEmpty()) {
            log.warn("Invalid cityId: {}", customerDTO.getCityId());
            return ResponseEntity.badRequest().body("Invalid cityId provided");
        }

        Customer customer = customerOptional.get();
        updateEntity(customer, customerDTO, cityOptional.get());
        customerService.save(customer);
        CustomerDTO updatedDTO = convertToDTO(customer);
        return ResponseEntity.ok(updatedDTO);
    }

    // Xóa khách hàng
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        log.info("Deleting customer with id: {}", id);

        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()) {
            log.warn("Customer not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Tìm kiếm khách hàng theo tên
    @GetMapping("/customers/search")
    public ResponseEntity<?> searchCustomers(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        log.info("Searching customers by name: {}, page: {}, size: {}, sortBy: {}", name, page, size, sortBy);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Customer> customers = customerService.searchByName(name, pageable);

        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<CustomerDTO> customerDTOs = customers.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customerDTOs);
    }

    // Lấy tất cả thành phố
    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.findAll();
        if (cities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cities);
    }

    // Chuyển từ entity sang DTO (CẬP NHẬT ĐỂ THÊM cityName)
    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setCityId(customer.getCity().getId());
        customerDTO.setCityName(customer.getCity().getNameCity()); // Thêm cityName
        return customerDTO;
    }

    // Chuyển từ DTO sang entity
    private Customer convertToEntity(CustomerDTO customerDTO, City city) {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setCity(city);
        return customer;
    }

    // Cập nhật entity từ DTO
    private void updateEntity(Customer customer, CustomerDTO customerDTO, City city) {
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setCity(city);
    }
}