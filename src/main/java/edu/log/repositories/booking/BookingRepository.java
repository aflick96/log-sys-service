package edu.log.repositories.booking;

import edu.log.models.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findById(long id);
    Booking findByBookingQuoteId(long id);
    Booking findByStatus(String status);
    Booking findByToAddress(String toAddress);
}
