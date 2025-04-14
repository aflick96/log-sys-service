/*
 * InvoicingController.java
 * 
 * This controller handles the invoicing operations, including retrieving all invoices and unpaid invoices.
 */

package edu.log.controllers;

import edu.log.dto.InvoiceDTO;
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
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        if (invoices.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No invoices found");
        }
        List<InvoiceDTO> invoiceDTOs = invoices.stream()
                .map(InvoiceDTO::new)
                .toList();
        return ResponseEntity.ok(invoiceDTOs);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<InvoiceDTO>> getUnpaidInvoices() {
        Optional<List<Invoice>> invoices = invoiceService.getUnpaidInvoices();
        if (invoices.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No unpaid invoices found");
        }
        List<InvoiceDTO> invoiceDTOs = invoices.get().stream()
                .map(InvoiceDTO::new)
                .toList();
        return ResponseEntity.ok(invoiceDTOs);
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<InvoiceDTO> payInvoice(@PathVariable("id") Long id) {
        try {
            Invoice paidInvoice = invoiceService.markAsPaid(id);
            return ResponseEntity.ok(new InvoiceDTO(paidInvoice));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to pay invoice", e);
        }
    }

}
