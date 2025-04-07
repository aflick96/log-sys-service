/*
 * WarehouseRepository.java
 * 
 * This interface defines the repository for the Warehouse entity. It extends JpaRepository to provide CRUD operations and custom methods to find a warehouse by its ID or name.
 */

package edu.log.repositories.warehouse;

import edu.log.models.warehouse.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Warehouse findById(long id);
    Warehouse findByName(String name);
}
