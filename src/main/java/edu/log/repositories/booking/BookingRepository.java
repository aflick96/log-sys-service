/*
 * BookingRepository.java
 * 
 * This interface defines the repository for the Booking entity. It extends JpaRepository to provide CRUD operations and custom query methods for finding bookings by various attributes.
 */

package edu.log.repositories.booking;

import edu.log.models.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findById(long id);
    Booking findByBookingQuoteId(long id);
    Booking findByStatus(String status);
    Booking findByToAddress(String toAddress);
    Optional<Booking> findByContainerId(String containerId);

}
