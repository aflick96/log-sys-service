package edu.log.models.booking;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "booking_quote")
public class BookingQuote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false, unique = true)
    @JsonBackReference
    private Booking booking;

    @Column(name = "price")
    private Double price;

    @Column(name = "estimated_delivery_time", nullable = false)
    private Integer estimatedDeliveryTime; // in days

    public BookingQuote() {}

    public BookingQuote(Booking booking, Double price, Integer estimatedDeliveryTime) {
        this.booking = booking;
        this.price = price;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
    public void setEstimatedDeliveryTime(Integer estimatedDeliveryTime) { this.estimatedDeliveryTime = estimatedDeliveryTime; }

    @Override
    public String toString() {
        return "BookingQuote{" +
                "id=" + id +
                ", price=" + price +
                ", estimatedDeliveryTime=" + estimatedDeliveryTime +
                '}';
    }
}
