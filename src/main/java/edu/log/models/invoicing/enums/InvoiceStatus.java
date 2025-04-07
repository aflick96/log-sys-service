/*
 * InvoiceStatus.java
 * 
 * This enum represents the different statuses an invoice can have. It is used in the Invoice model to specify the current status of an invoice.
 */

package edu.log.models.invoicing.enums;

public enum InvoiceStatus {
    UNPAID,
    PAID,
    CANCELLED
}
