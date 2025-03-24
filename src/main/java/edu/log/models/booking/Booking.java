package edu.log.models.booking;

import edu.log.models.warehouse.Warehouse;
import edu.log.models.booking.enums.*;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "to_address")
    private String toAddress;

    @Column(name = "description")
    private String description;

    @Column(name = "service_type")
    @Enumerated(EnumType.STRING)
    private BookingServiceType serviceType;

    @Column(name = "volume")
    private Double volume;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "distance")
    private Double distance;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id", nullable = false)
    private Warehouse warehouse;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private BookingQuote bookingQuote;

    public Booking() {}

    public Booking(Warehouse warehouse, String toAddress, String description, BookingServiceType serviceType, Double volume, Double weight, Double distance) {
        this.warehouse = warehouse;
        this.toAddress = toAddress;
        this.description = description;
        this.serviceType = serviceType;
        this.volume = volume;
        this.weight = weight;
        this.status = BookingStatus.PENDING;
        this.distance = distance;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }

    public String getToAddress() { return toAddress; }
    public void setToAddress(String toAddress) { this.toAddress = toAddress; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BookingServiceType getServiceType() { return serviceType; }
    public void setServiceType(BookingServiceType serviceType) { this.serviceType = serviceType; }

    public Double getVolume() { return volume; }
    public void setVolume(Double volume) { this.volume = volume; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }

    public BookingQuote getBookingQuote() { return bookingQuote; }
    public void setBookingQuote(BookingQuote bookingQuote) { this.bookingQuote = bookingQuote; }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", warehouse=" + warehouse +
                ", toAddress='" + toAddress + '\'' +
                ", description='" + description + '\'' +
                ", serviceType=" + serviceType +
                ", volume=" + volume +
                ", weight=" + weight +
                ", status=" + status +
                ", distance=" + distance +
                ", bookingQuote=" + bookingQuote +
                '}';
    }
}
