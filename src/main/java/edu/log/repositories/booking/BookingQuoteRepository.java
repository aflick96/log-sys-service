package edu.log.repositories.booking;

import edu.log.models.booking.BookingQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingQuoteRepository extends JpaRepository<BookingQuote, Long> {
    BookingQuote findById(long id);
    BookingQuote findByBookingId(long id);
    BookingQuote findByPrice(double price);
}
