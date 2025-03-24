package edu.log.controllers;

import edu.log.models.shipping.ShippingStatus;
import edu.log.models.shipping.enums.ShippingStatusType;
import edu.log.services.shipping.ShippingStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/shipping")
public class ShippingStatusController {
    private final ShippingStatusService s;

    public ShippingStatusController(ShippingStatusService s) {
        this.s = s;
    }

    // Get shipping status by booking id
    @GetMapping("/{bookingId}")
    public ResponseEntity<ShippingStatus> getShippingStatusByBookingId(@PathVariable("bookingId") Long bookingId) {
        ShippingStatus shippingStatus = s.getShippingStatusByBookingId(bookingId);
        if (shippingStatus == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipping status not found for booking id: " + bookingId);
        return ResponseEntity.ok(shippingStatus);
    }

    // Get all shipments by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShippingStatus>> getShipmentsByStatus(@PathVariable("status") ShippingStatusType status) {
        List<ShippingStatus> shipments = s.getShipmentsByStatus(status);
        if (shipments.isEmpty()) throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No shipments found with status: " + status);
        return ResponseEntity.ok(shipments);
    }

    // Create a new shipping status
    @PostMapping
    public ResponseEntity<ShippingStatus> createShippingStatus(
        @RequestParam Long bookingId,
        @RequestParam ShippingStatusType status,
        @RequestParam Long warehouseId
    ) {
        ShippingStatus shippingStatus = s.createShippingStatus(bookingId, status, warehouseId);
        if (shippingStatus == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shipping status could not be created");
        return ResponseEntity.status(201).body(shippingStatus);
    }

    // Update shipping status
    @PutMapping("/{bookingId}")
    public ResponseEntity<ShippingStatus> updateShippingStatus(
        @PathVariable("bookingId") Long bookingId,
        @RequestParam ShippingStatusType status
    ) {
        ShippingStatus shippingStatus = s.updateShippingStatus(bookingId, status);
        if (shippingStatus == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shipping status could not be updated");
        return ResponseEntity.ok(shippingStatus);
    }
}
