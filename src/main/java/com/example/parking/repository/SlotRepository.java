package com.example.parking.repository;

import com.example.parking.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    Optional<Slot> findFirstByAvailableTrueAndSlotTypeIgnoreCase(
            String slotType
    );
}