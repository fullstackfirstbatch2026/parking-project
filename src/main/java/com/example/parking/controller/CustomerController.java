package com.example.parking.controller;

import com.example.parking.entity.Customer;
import com.example.parking.repository.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository repository;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Customer> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return repository.save(customer);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id,
                           @RequestBody Customer customer) {

        Customer existing = repository.findById(id).orElseThrow();

        existing.setName(customer.getName());
        existing.setPhone(customer.getPhone());
        existing.setEmail(customer.getEmail());

        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "Customer deleted successfully";
    }
}