package edu.log.services.warehouse;

import edu.log.models.warehouse.Warehouse;
import edu.log.repositories.warehouse.WarehouseRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {
    private final WarehouseRepository w_repo;

    public WarehouseService(WarehouseRepository w_repo) {
        this.w_repo = w_repo;
    }

    // Create a new warehouse
    public Warehouse createWarehouse(String name, String address) {
        if (w_repo.findByName(name) != null) throw new IllegalArgumentException("Warehouse already exists with name: " + name);
        Warehouse warehouse = new Warehouse(name, address);
        return w_repo.save(warehouse);
    }

    // Get a warehouse by id
    public Warehouse getWarehouseById(Long id) {
        Optional<Warehouse> warehouse = w_repo.findById(id);
        if (warehouse.isEmpty()) throw new IllegalArgumentException("Warehouse not found with id: " + id);
        return warehouse.get();
    }

    //Get all warehouses
    public List<Warehouse> getAllWarehouses() {
        return w_repo.findAll();
    }
}
