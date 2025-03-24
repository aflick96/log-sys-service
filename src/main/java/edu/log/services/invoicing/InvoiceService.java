package edu.log.services.invoicing;

import org.springframework.stereotype.Service;
import edu.log.models.booking.Booking;
import edu.log.models.invoicing.Invoice;
import edu.log.models.invoicing.enums.InvoiceStatus;
import edu.log.repositories.invoicing.InvoiceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepo;

    public InvoiceService(InvoiceRepository invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
    }

    public Invoice generateInvoice(Booking booking) {
        if (invoiceRepo.findByBookingId(booking.getId()) != null) {
            throw new IllegalStateException("Invoice already exists for booking id: " + booking.getId());
        }
        double totalAmount = booking.getBookingQuote().getPrice();
        Invoice invoice = new Invoice(booking, totalAmount);
        return invoiceRepo.save(invoice);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepo.findAll();
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepo.findById(id);
    }

    public Optional<List<Invoice>> getUnpaidInvoices() {
        return Optional.of(invoiceRepo.findAll().stream().filter(invoice -> invoice.getStatus() == InvoiceStatus.UNPAID).toList());
    }

    public Invoice markAsPaid(Long id) {
        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        invoice.setStatus(InvoiceStatus.PAID);
        return invoiceRepo.save(invoice);
    }
}
