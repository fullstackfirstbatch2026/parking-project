package com.example.parking.controller;

import com.example.parking.entity.Slot;
import com.example.parking.repository.SlotRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slots")
@CrossOrigin(origins = "*")
public class SlotController {

    private final SlotRepository slotRepository;

    public SlotController(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    // Get all slots
    @GetMapping
    public List<Slot> getAllSlots() {
        return slotRepository.findAll();
    }

    // Get slot by ID
    @GetMapping("/{id}")
    public Slot getSlotById(@PathVariable Long id) {
        return slotRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Slot not found with ID: " + id));
    }

    // Add new slot
    @PostMapping
    public Slot createSlot(@RequestBody Slot slot) {

        if (slot.getAvailable() == null) {
            slot.setAvailable(true);
        }

        return slotRepository.save(slot);
    }

    // Update slot
    @PutMapping("/{id}")
    public Slot updateSlot(
            @PathVariable Long id,
            @RequestBody Slot slotDetails) {

        Slot slot = slotRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Slot not found with ID: " + id));

        slot.setSlotNumber(slotDetails.getSlotNumber());
        slot.setSlotType(slotDetails.getSlotType());
        slot.setAvailable(slotDetails.getAvailable());

        return slotRepository.save(slot);
    }

    // Delete slot
    @DeleteMapping("/{id}")
    public String deleteSlot(@PathVariable Long id) {

        Slot slot = slotRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Slot not found with ID: " + id));

        slotRepository.delete(slot);

        return "Slot deleted successfully";
    }
}