package com.capsule.repository;

import com.capsule.model.MedicineInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface InventoryRepository
        extends JpaRepository<MedicineInventory, Long> {

    // Sir wali query — expiry 1 year aage + stock > 0
    @Query(value = """
            SELECT * FROM medicine_inventory
            WHERE name IN (:names)
            AND expiry_date > DATE_ADD(NOW(), INTERVAL 1 YEAR)
            AND stock > 0
            ORDER BY stock DESC
            """, nativeQuery = true)
    List<MedicineInventory> findAvailableMedicines(
            @Param("names") List<String> names
    );
}
