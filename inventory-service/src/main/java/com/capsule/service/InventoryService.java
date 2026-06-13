package com.capsule.service;

import com.capsule.model.MedicineInventory;
import com.capsule.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    // Add medicine
    public MedicineInventory addMedicine(MedicineInventory medicine) {
        return inventoryRepository.save(medicine);
    }

    // Get all medicines
    public List<MedicineInventory> getAllMedicines() {
        return inventoryRepository.findAll();
    }

    // Search by name list — expiry filter applied
    public List<MedicineInventory> searchMedicines(List<String> names) {
        return inventoryRepository.findAvailableMedicines(names);
    }

    // Delete by id
    public void deleteMedicine(Long id) {
        inventoryRepository.deleteById(id);
    }
}
