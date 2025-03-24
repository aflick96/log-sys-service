package edu.log.models.shipping;

import edu.log.models.shipping.enums.ShippingStatusType;
import edu.log.models.booking.Booking;
import edu.log.models.warehouse.Warehouse;
import jakarta.persistence.*;

@Entity
@Table(name = "shipping_status")
public class ShippingStatus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ShippingStatusType status;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id", nullable = false)
    private Warehouse warehouse;

    public ShippingStatus() {}

    public ShippingStatus(Booking booking, ShippingStatusType status, Warehouse warehouse) {
        this.booking = booking;
        this.status = status;
        this.warehouse = warehouse;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public ShippingStatusType getStatus() { return status; }
    public void setStatus(ShippingStatusType status) { this.status = status; }

    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }

    @Override
    public String toString() {
        return "ShippingStatus{" +
                "id=" + id +
                ", booking=" + booking +
                ", status=" + status +
                ", warehouse=" + warehouse +
                '}';
    }
}
