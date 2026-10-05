package com.example.parking.repository;

import com.example.parking.entity.ParkingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkingTransactionRepository
        extends JpaRepository<ParkingTransaction, Long> {

    @Query("""
            SELECT pt
            FROM ParkingTransaction pt
            JOIN FETCH pt.vehicle v
            JOIN FETCH pt.slot s
            """)
    List<ParkingTransaction> findTransactionsWithVehicleAndSlot();
}