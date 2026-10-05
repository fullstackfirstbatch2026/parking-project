package com.example.parking.controller;

import com.example.parking.entity.Vehicle;
import com.example.parking.repository.VehicleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleRepository repository;

    public VehicleController(VehicleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Vehicle> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Vehicle create(@RequestBody Vehicle vehicle) {
        return repository.save(vehicle);
    }

    @PutMapping("/{id}")
    public Vehicle update(@PathVariable Long id,
                          @RequestBody Vehicle vehicle) {

        Vehicle existing = repository.findById(id).orElseThrow();

        existing.setVehicleNumber(vehicle.getVehicleNumber());
        existing.setVehicleType(vehicle.getVehicleType());
        existing.setCustomer(vehicle.getCustomer());

        return repository.save(existing);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "Vehicle deleted successfully";
    }
}