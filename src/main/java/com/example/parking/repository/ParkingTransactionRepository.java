package com.example.parking.repository;

import com.example.parking.entity.ParkingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkingTransactionRepository
        extends JpaRepository<ParkingTransaction, Long> {

    // JOIN: Get parking transactions with vehicle and slot details
    @Query("""
        SELECT p
        FROM ParkingTransaction p
        JOIN FETCH p.vehicle v
        JOIN FETCH p.slot s
        """)
    List<ParkingTransaction> findTransactionsWithVehicleAndSlot();

    // Calculate total revenue
    @Query("""
        SELECT COALESCE(SUM(p.fee), 0)
        FROM ParkingTransaction p
        """)
    Double getTotalRevenue();
}