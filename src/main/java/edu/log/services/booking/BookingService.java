/*
 * BookingService.java
 * 
 * This service class handles the business logic for managing bookings. It includes methods to create a new booking, retrieve all bookings, and get a booking by its ID. It also integrates with other services like Google Maps for distance calculation and InvoiceService for generating invoices.
 */

package edu.log.services.booking;

import edu.log.models.booking.Booking;
import edu.log.models.booking.BookingQuote;
import edu.log.models.booking.enums.BookingStatus;
import edu.log.models.warehouse.Warehouse;
import edu.log.repositories.booking.BookingRepository;
import edu.log.repositories.invoicing.InvoiceRepository;
import edu.log.repositories.warehouse.WarehouseRepository;
import edu.log.services.google.GoogleMapsService;
import edu.log.services.invoicing.InvoiceService;
import edu.log.utils.BookingCalculator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository b_repo;
    private final InvoiceRepository invoiceRepo;
    private final GoogleMapsService gms;
    private final WarehouseRepository w_repo;
    private final InvoiceService invoiceService;

    public BookingService(BookingRepository b_repo, InvoiceRepository invoiceRepo, GoogleMapsService gms,
            WarehouseRepository w_repo, InvoiceService invoiceService) {
        this.b_repo = b_repo;
        this.gms = gms;
        this.w_repo = w_repo;
        this.invoiceService = invoiceService;
        this.invoiceRepo = invoiceRepo;
    }

    public Optional<Booking> getBookingByContainerId(String containerId) {
        return b_repo.findByContainerId(containerId);
    }

    // Create a new booking
    public Booking createBooking(Booking booking) {
        if (booking.getWarehouse() == null)
            throw new IllegalArgumentException("Warehouse cannot be null");
    
        Warehouse warehouse = w_repo.findById(booking.getWarehouse().getId())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found with id: " + booking.getWarehouse().getId()));
    
        // Validate toAddress
        String toAddress = booking.getToAddress();
        if (toAddress == null || toAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking must have a destination address.");
        }
        if (toAddress.length() < 10) {
            throw new IllegalArgumentException("Destination address must be at least 10 characters long.");
        }
    
        // Validate address using Google Maps
        double distance = gms.getDistance(warehouse.getAddress(), toAddress);
        if (distance == 0.0) {
            throw new IllegalArgumentException("Invalid destination address. Please provide a valid location.");
        }
    
        // Set remaining booking details
        booking.setWarehouse(warehouse);
        booking.setStatus(BookingStatus.PENDING);
        booking.setDistance(distance);
    
        double price = BookingCalculator.computePrice(booking);
        int estimatedDeliveryTime = BookingCalculator.computeEstimatedDeliveryTime(booking);
    
        BookingQuote bookingQuote = new BookingQuote(booking, price, estimatedDeliveryTime);
        booking.setBookingQuote(bookingQuote);
    
        Booking savedBooking = b_repo.save(booking);
        invoiceService.generateInvoice(savedBooking);
    
        return savedBooking;
    }
    
    // Get all bookings
    public List<Booking> getAllBookings() {
        return b_repo.findAll();
    }

    // Get booking by id
    public Optional<Booking> getBookingById(long id) {
        return Optional.ofNullable(b_repo.findById(id));
    }

    public void deleteAllBookings() {
        invoiceRepo.deleteAll();
        b_repo.deleteAll();
    }

}
