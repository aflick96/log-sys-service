package edu.log.models.invoicing;

import edu.log.models.booking.Booking;
import edu.log.models.invoicing.enums.InvoiceStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.UNPAID;

    private LocalDateTime createdAt;

    // Constructors
    public Invoice() {
        this.createdAt = LocalDateTime.now();
        this.status = InvoiceStatus.UNPAID;
    }

    public Invoice(Booking booking, double totalAmount) {
        this.booking = booking;
        this.totalAmount = totalAmount;
        this.createdAt = LocalDateTime.now();
        this.status = InvoiceStatus.UNPAID;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public boolean isPaid() {
        return this.status == InvoiceStatus.PAID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
