package edu.log.controllers;

import edu.log.models.booking.Booking;
import edu.log.models.booking.enums.BookingServiceType;
import edu.log.models.warehouse.Warehouse;
import edu.log.dto.BookingRequestDTO;
import edu.log.services.booking.BookingService;
import edu.log.services.warehouse.WarehouseService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final WarehouseService warehouseService;

    public BookingController(BookingService bookingService, WarehouseService warehouseService) {
        this.bookingService = bookingService;
        this.warehouseService = warehouseService;
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No bookings found");
        }
        return ResponseEntity.ok(bookings);
    }

    // Get booking by id
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable("id") long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        }
        return ResponseEntity.ok(booking.get());
    }

    // Create a new booking using DTO
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequestDTO request) {
        try {
            if (request.getWarehouse() == null || request.getWarehouse().getId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Warehouse ID is required.");
            }
    
            Warehouse warehouse = warehouseService.getWarehouseById(request.getWarehouse().getId());

            Booking booking = new Booking(
                    warehouse,
                    request.getToAddress(),
                    request.getDescription(),
                    BookingServiceType.valueOf(request.getServiceType()),
                    request.getVolume(),
                    request.getWeight(),
                    null
            );
    
            Booking createdBooking = bookingService.createBooking(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
    

    // Delete all bookings
    @DeleteMapping
    public ResponseEntity<Void> deleteAllBookings() {
        try {
            bookingService.deleteAllBookings();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace(); // Or use a logger
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting bookings", e);
        }
    }
}
