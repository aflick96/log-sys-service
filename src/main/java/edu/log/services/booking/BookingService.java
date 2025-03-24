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

    public BookingService(BookingRepository b_repo,InvoiceRepository invoiceRepo, GoogleMapsService gms, WarehouseRepository w_repo, InvoiceService invoiceService) {
        this.b_repo = b_repo;
        this.gms = gms;
        this.w_repo = w_repo;
        this.invoiceService = invoiceService;
        this.invoiceRepo = invoiceRepo;
    }

    // Create a new booking
    public Booking createBooking(Booking booking) {
        if (booking.getWarehouse() == null)
            throw new IllegalArgumentException("Warehouse cannot be null");

        // Check if warehouse exists
        Warehouse warehouse = w_repo.findById(booking.getWarehouse().getId())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found with id: " + booking.getWarehouse().getId()));

        // Check if booking has a valid toAddress
        if (booking.getToAddress() == null || booking.getToAddress().isEmpty())
            throw new IllegalArgumentException("Booking must have a valid toAddress");

        // Compute distance using Google Maps service
        double distance = gms.getDistance(warehouse.getAddress(), booking.getToAddress());

        // Set warehouse, status, and distance in booking
        booking.setWarehouse(warehouse);
        booking.setStatus(BookingStatus.PENDING);
        booking.setDistance(distance);

        // Compute price and estimated delivery time using BookingCalculator
        double price = BookingCalculator.computePrice(booking);
        int estimatedDeliveryTime = BookingCalculator.computeEstimatedDeliveryTime(booking);

        // Create booking quote and set it in booking
        BookingQuote bookingQuote = new BookingQuote(booking, price, estimatedDeliveryTime);
        booking.setBookingQuote(bookingQuote);

        // Save booking
        Booking savedBooking = b_repo.save(booking);

        // Generate invoice
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
