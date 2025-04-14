/*
 * WarehouseService.java
 * 
 * This service class handles the business logic for warehouse operations, including creating a new warehouse, retrieving a warehouse by id, and getting all warehouses.
 */

package edu.log.services.warehouse;

import edu.log.models.warehouse.Warehouse;
import edu.log.repositories.warehouse.WarehouseRepository;
import edu.log.services.google.GoogleMapsService;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {
    private final WarehouseRepository w_repo;
    private final GoogleMapsService mapsService;

    public WarehouseService(WarehouseRepository w_repo, GoogleMapsService mapsService) {
        this.w_repo = w_repo;
        this.mapsService = mapsService;
    }
    // Create a new warehouse
    public Warehouse createWarehouse(String name, String address) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse name must not be empty");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse address must not be empty");
        }
        if (address.length() < 10) {
            throw new IllegalArgumentException("Warehouse address must be at least 10 characters long");
        }
        if (!mapsService.isAddressValid(address)) {
            throw new IllegalArgumentException("Invalid warehouse address. Please enter a real address.");
        }
        if (w_repo.findByName(name) != null) {
            throw new IllegalArgumentException("Warehouse already exists with name: " + name);
        }
    
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
