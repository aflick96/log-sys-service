package edu.log.dto;

import edu.log.models.invoicing.Invoice;

public class InvoiceDTO {  // <- this MUST match the filename
    private Long id;
    private double totalAmount;
    private String status;

    public InvoiceDTO(Invoice invoice) {
        this.id = invoice.getId();
        this.totalAmount = invoice.getTotalAmount();
        this.status = invoice.getStatus().name();
    }

    public Long getId() {
        return id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }
}
