/*
 * InvoiceService.java
 * 
 * This service class handles the business logic for managing invoices, including generating new invoices, retrieving all invoices, and marking invoices as paid. It interacts with the InvoiceRepository to perform CRUD operations on Invoice entities.
 */

package edu.log.services.invoicing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.log.models.booking.Booking;
import edu.log.models.invoicing.Invoice;
import edu.log.models.invoicing.enums.InvoiceStatus;
import edu.log.repositories.invoicing.InvoiceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepo;

    public InvoiceService() {}

    // Method to generate an invoice for a booking
    public Invoice generateInvoice(Booking booking) {
        if (invoiceRepo.findByBookingId(booking.getId()) != null) {
            throw new IllegalStateException("Invoice already exists for booking id: " + booking.getId());
        }
        double totalAmount = booking.getBookingQuote().getPrice();
        Invoice invoice = new Invoice(booking, totalAmount);
        return invoiceRepo.save(invoice);
    }

    // Method to retrieve all invoices
    public List<Invoice> getAllInvoices() {
        return invoiceRepo.findAll();
    }

    // Method to retrieve an invoice by its ID
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepo.findById(id);
    }

    // Method to retrieve all unpaid invoices
    public Optional<List<Invoice>> getUnpaidInvoices() {
        return Optional.of(
                invoiceRepo.findAll().stream().filter(invoice -> invoice.getStatus() == InvoiceStatus.UNPAID).toList());
    }

    // Method to mark an invoice as paid
    public Invoice markAsPaid(Long id) {
        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
    
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new IllegalStateException("Invoice is already paid");
        }
    
        invoice.setStatus(InvoiceStatus.PAID);
        return invoiceRepo.save(invoice);
    }
}
