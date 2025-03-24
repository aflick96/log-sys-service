package edu.log.services.shipping;

import edu.log.models.shipping.ShippingStatus;
import edu.log.models.shipping.enums.ShippingStatusType;
import edu.log.models.warehouse.Warehouse;
import edu.log.models.booking.Booking;
import edu.log.repositories.shipping.ShippingStatusRepository;
import edu.log.repositories.warehouse.WarehouseRepository;
import edu.log.repositories.booking.BookingRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ShippingStatusService {
    private final ShippingStatusRepository ss_repo;
    private final WarehouseRepository w_repo;
    private final BookingRepository b_repo;

    public ShippingStatusService(ShippingStatusRepository ss_repo, WarehouseRepository w_repo, BookingRepository b_repo) {
        this.ss_repo = ss_repo;
        this.w_repo = w_repo;
        this.b_repo = b_repo;
    }

    // Create a new shipping status
    public ShippingStatus createShippingStatus(Long bookingId, ShippingStatusType status, Long warehouseId) {
        Optional<Booking> booking = b_repo.findById(bookingId);
        if (booking.isEmpty()) throw new IllegalArgumentException("Booking not found with id: " + bookingId);

        Optional<Warehouse> warehouse = w_repo.findById(warehouseId);
        if (warehouse.isEmpty()) throw new IllegalArgumentException("Warehouse not found with id: " + warehouseId);

        ShippingStatus shippingStatus = new ShippingStatus(booking.get(), status, warehouse.get());
        return ss_repo.save(shippingStatus);
    }

    // Get shipping status by booking id
    public ShippingStatus getShippingStatusByBookingId(Long bookingId) {
        return ss_repo.findByBooking_Id(bookingId);
    }

    // Update shipping status
    public ShippingStatus updateShippingStatus(Long bookingId, ShippingStatusType status) {
        ShippingStatus shippingStatus = ss_repo.findByBooking_Id(bookingId);
        if (shippingStatus == null) throw new IllegalArgumentException("Shipping status not found with booking id: " + bookingId);

        shippingStatus.setStatus(status);
        return ss_repo.save(shippingStatus);
    }

    // Get all shipments by status
    public List<ShippingStatus> getShipmentsByStatus(ShippingStatusType status) {
        return ss_repo.findByStatus(status);
    }
}
