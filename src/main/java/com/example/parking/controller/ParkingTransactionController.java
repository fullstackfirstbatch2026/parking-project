package com.example.parking.controller;

import com.example.parking.entity.ParkingTransaction;
import com.example.parking.service.ParkingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking")
@CrossOrigin(origins = "*")
public class ParkingTransactionController {

    private final ParkingService parkingService;

    public ParkingTransactionController(
            ParkingService parkingService) {

        this.parkingService = parkingService;
    }

    @PostMapping("/allocate/{vehicleId}")
    public ParkingTransaction allocateSlot(
            @PathVariable Long vehicleId) {

        return parkingService.allocateSlot(vehicleId);
    }

    @GetMapping("/transactions")
    public List<ParkingTransaction> getTransactions() {

        return parkingService.getAllTransactions();
    }

    @PutMapping("/exit/{transactionId}")
    public ParkingTransaction exitVehicle(
            @PathVariable Long transactionId) {

        return parkingService.exitVehicle(transactionId);
    }

    @GetMapping("/revenue")
    public double getTotalRevenue() {

        return parkingService.getTotalRevenue();
    }
}