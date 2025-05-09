/*
 * WarehouseController.java
 * 
 * This controller handles the warehouse operations, including retrieving all warehouses, getting a warehouse by id, and creating a new warehouse.
 */

package edu.log.controllers;

import edu.log.models.warehouse.Warehouse;
import edu.log.services.warehouse.WarehouseService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService w;

    // Constructor
    public WarehouseController() {}

    // Get all warehouses
    @GetMapping
    public ResponseEntity<List<Warehouse>> getAllWarehouses() {
        List<Warehouse> warehouses = w.getAllWarehouses();
        if (warehouses.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No warehouses found");
        return ResponseEntity.ok(warehouses);
    }

    // Get a warehouse by id
    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable("id") Long id) {
        Warehouse warehouse = w.getWarehouseById(id);
        if (warehouse == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Warehouse not found with id: " + id);
        return ResponseEntity.ok(warehouse);
    }

    // Create a new warehouse
    @PostMapping
    public ResponseEntity<?> createWarehouse(@Valid @RequestBody Warehouse warehouse) {
        Warehouse saved = w.createWarehouse(
                warehouse.getName(),
                warehouse.getAddress());
        return ResponseEntity.ok(saved);
    }
}
