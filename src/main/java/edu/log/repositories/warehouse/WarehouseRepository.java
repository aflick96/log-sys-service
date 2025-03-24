package edu.log.repositories.warehouse;

import edu.log.models.warehouse.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Warehouse findById(long id);
    Warehouse findByName(String name);
}
