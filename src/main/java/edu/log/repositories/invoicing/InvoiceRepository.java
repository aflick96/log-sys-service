/*
 * InvoiceRepository.java
 * 
 * This repository interface is used to perform CRUD operations on the Invoice entity. It extends JpaRepository to leverage Spring Data JPA's capabilities for database interactions.
 */

package edu.log.repositories.invoicing;

import edu.log.models.invoicing.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findByBookingId(long bookingId);
}
