package edu.log.repositories.invoicing;

import edu.log.models.invoicing.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findByBookingId(long bookingId);
}
