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

    // ==========================================
    // ALLOCATE PARKING SLOT
    // ==========================================

    public ParkingTransaction allocateSlot(Long vehicleId) {

        // Find vehicle
        Vehicle vehicle = vehicleRepository
                .findById(vehicleId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vehicle not found with ID: " + vehicleId
                        ));

        // Find available slot matching vehicle type
        Slot slot = slotRepository
                .findFirstByAvailableTrueAndSlotTypeIgnoreCase(
                        vehicle.getVehicleType()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "No available "
                                        + vehicle.getVehicleType()
                                        + " slot found"
                        ));

        // Mark slot as occupied
        slot.setAvailable(false);
        slotRepository.save(slot);

        // Create parking transaction
        ParkingTransaction transaction =
                new ParkingTransaction();

        transaction.setVehicle(vehicle);
        transaction.setSlot(slot);

        // Entry time
        transaction.setEntryTime(
                LocalDateTime.now()
        );

        // Vehicle has not exited yet
        transaction.setExitTime(null);

        // Initial fee
        transaction.setFee(0.0);

        // Save transaction
        return transactionRepository.save(transaction);
    }

    // ==========================================
    // GET ALL PARKING TRANSACTIONS
    // ==========================================

    public List<ParkingTransaction> getAllTransactions() {

        return transactionRepository
                .findTransactionsWithVehicleAndSlot();
    }

    // ==========================================
    // EXIT VEHICLE
    // ==========================================

    public ParkingTransaction exitVehicle(Long transactionId) {

        // Find transaction
        ParkingTransaction transaction =
                transactionRepository
                        .findById(transactionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found with ID: "
                                                + transactionId
                                ));

        // Prevent exiting twice
        if (transaction.getExitTime() != null) {
            throw new RuntimeException(
                    "Vehicle has already exited"
            );
        }

        // Set exit time
        LocalDateTime exitTime =
                LocalDateTime.now();

        transaction.setExitTime(exitTime);

        // Get entry time
        LocalDateTime entryTime =
                transaction.getEntryTime();

        // Calculate parking duration
        long minutes =
                Duration.between(
                        entryTime,
                        exitTime
                ).toMinutes();

        // Minimum 1 hour
        long hours =
                Math.max(
                        1,
                        (minutes + 59) / 60
                );

        // Parking fee = ₹50 per hour
        double fee =
                hours * 50.0;

        transaction.setFee(fee);

        // Make slot available again
        Slot slot =
                transaction.getSlot();

        slot.setAvailable(true);

        slotRepository.save(slot);

        // Save transaction
        return transactionRepository.save(transaction);
    }

    // ==========================================
    // TOTAL REVENUE
    // ==========================================

    public Double getTotalRevenue() {

        return transactionRepository
                .getTotalRevenue();
    }
}