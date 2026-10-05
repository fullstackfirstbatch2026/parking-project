package com.example.parking.service;

import com.example.parking.entity.ParkingTransaction;
import com.example.parking.entity.Slot;
import com.example.parking.entity.Vehicle;
import com.example.parking.repository.ParkingTransactionRepository;
import com.example.parking.repository.SlotRepository;
import com.example.parking.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingService {

    private final ParkingTransactionRepository transactionRepository;
    private final VehicleRepository vehicleRepository;
    private final SlotRepository slotRepository;

    public ParkingService(
            ParkingTransactionRepository transactionRepository,
            VehicleRepository vehicleRepository,
            SlotRepository slotRepository) {

        this.transactionRepository = transactionRepository;
        this.vehicleRepository = vehicleRepository;
        this.slotRepository = slotRepository;
    }

    public ParkingTransaction allocateSlot(Long vehicleId) {

        Vehicle vehicle = vehicleRepository
                .findById(vehicleId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vehicle not found with ID: " + vehicleId));

        Slot slot = slotRepository
                .findFirstByAvailableTrue()
                .orElseThrow(() ->
                        new RuntimeException(
                                "No parking slot available"));

        slot.setAvailable(false);

        slotRepository.save(slot);

        ParkingTransaction transaction =
                new ParkingTransaction();

        transaction.setVehicle(vehicle);
        transaction.setSlot(slot);
        transaction.setEntryTime(LocalDateTime.now());
        transaction.setExitTime(null);
        transaction.setFee(0.0);

        return transactionRepository.save(transaction);
    }

    public List<ParkingTransaction> getAllTransactions() {

        return transactionRepository
                .findTransactionsWithVehicleAndSlot();
    }

    public ParkingTransaction exitVehicle(Long transactionId) {

        ParkingTransaction transaction =
                transactionRepository
                        .findById(transactionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found with ID: "
                                                + transactionId));

        if (transaction.getExitTime() != null) {

            throw new RuntimeException(
                    "Vehicle has already exited");
        }

        LocalDateTime exitTime = LocalDateTime.now();

        transaction.setExitTime(exitTime);

        double fee = calculateParkingFee(
                transaction.getEntryTime(),
                exitTime
        );

        transaction.setFee(fee);

        Slot slot = transaction.getSlot();

        if (slot != null) {

            slot.setAvailable(true);

            slotRepository.save(slot);
        }

        return transactionRepository.save(transaction);
    }

    public double calculateParkingFee(
            LocalDateTime entryTime,
            LocalDateTime exitTime) {

        long minutes = Duration
                .between(entryTime, exitTime)
                .toMinutes();

        long hours =
                (long) Math.ceil(minutes / 60.0);

        if (hours <= 0) {
            hours = 1;
        }

        double ratePerHour = 50.0;

        return hours * ratePerHour;
    }

    public double getTotalRevenue() {

        List<ParkingTransaction> transactions =
                transactionRepository.findAll();

        double total = 0.0;

        for (ParkingTransaction transaction : transactions) {

            if (transaction.getFee() != null) {
                total += transaction.getFee();
            }
        }

        return total;
    }
}