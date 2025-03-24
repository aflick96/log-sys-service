package edu.log.controllers;

import edu.log.models.invoicing.Invoice;
import edu.log.services.invoicing.InvoiceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/invoices")
public class InvoicingController {
    private final InvoiceService invoiceService;

    public InvoicingController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        if (invoices.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No invoices found");
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<Invoice>> getUnpaidInvoices() {
        Optional<List<Invoice>> invoices = invoiceService.getUnpaidInvoices();
        if (invoices.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No unpaid invoices found");
        }
        return ResponseEntity.ok(invoices.get());
    }
}
