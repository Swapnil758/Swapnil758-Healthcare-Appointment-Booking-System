package com.capsule.controller;

import com.capsule.model.MedicineInventory;
import com.capsule.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // ─── 1. Medicine add karo ────────────────────────────
    // POST http://localhost:8080/api/inventory/add
    // Body: raw JSON
    // {
    //   "name": "Paracetamol",
    //   "brand": "Calpol",
    //   "category": "Fever",
    //   "dosage": "500mg",
    //   "price": 18.00,
    //   "stock": 50,
    //   "expiryDate": "2027-06-01"
    // }
    @PostMapping("/add")
    public ResponseEntity<?> addMedicine(
            @RequestBody MedicineInventory medicine) {
        MedicineInventory saved = inventoryService.addMedicine(medicine);
        System.out.println("Medicine added: " + saved.getName());
        return ResponseEntity.ok(saved);
    }

    // ─── 2. Sab medicines dekho ──────────────────────────
    // GET http://localhost:8080/api/inventory/all
    @GetMapping("/all")
    public ResponseEntity<?> getAllMedicines() {
        return ResponseEntity.ok(inventoryService.getAllMedicines());
    }

    // ─── 3. Prescription medicines search karo ───────────
    // POST http://localhost:8080/api/inventory/search
    // Body: ["Paracetamol", "Amoxicillin"]
    @PostMapping("/search")
    public ResponseEntity<?> searchMedicines(
            @RequestBody List<String> medicineNames) {
        List<MedicineInventory> result =
                inventoryService.searchMedicines(medicineNames);
        return ResponseEntity.ok(result);
    }

    // ─── 4. Medicine delete karo ─────────────────────────
    // DELETE http://localhost:8080/api/inventory/1
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedicine(@PathVariable Long id) {
        inventoryService.deleteMedicine(id);
        return ResponseEntity.ok("Medicine deleted! ✅");
    }

    // ─── 5. Health check ─────────────────────────────────
    // GET http://localhost:8080/api/inventory/health
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Inventory Service running! ✅");
    }
}
