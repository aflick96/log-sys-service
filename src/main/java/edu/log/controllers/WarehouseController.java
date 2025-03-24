package edu.log.controllers;

import edu.log.models.warehouse.Warehouse;
import edu.log.services.warehouse.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {
    private final WarehouseService w;

    public WarehouseController(WarehouseService w) {
        this.w = w;
    }

    // Get all warehouses
    @GetMapping
    public ResponseEntity<List<Warehouse>> getAllWarehouses() {
        List<Warehouse> warehouses = w.getAllWarehouses();
        if (warehouses.isEmpty()) throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No warehouses found");
        return ResponseEntity.ok(warehouses);
    }

    // Get a warehouse by id
    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable("id") Long id) {
        Warehouse warehouse = w.getWarehouseById(id);
        if (warehouse == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Warehouse not found with id: " + id);
        return ResponseEntity.ok(warehouse);
    }

    // Create a new warehouse
    @PostMapping
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody Warehouse wh) {
        Warehouse warehouse = w.createWarehouse(wh.getName(), wh.getAddress());
        if (warehouse == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Warehouse could not be created");
        return ResponseEntity.status(201).body(warehouse);
    }
}
