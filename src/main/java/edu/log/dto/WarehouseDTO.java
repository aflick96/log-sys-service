/*
 * WarehouseDTO.java
 * 
 * This DTO represents a warehouse object with only the ID field. It is used in BookingRequestDTO to transfer warehouse information from the client to the server.
 */

package edu.log.dto;

public class WarehouseDTO {
    private Long id;

    // Constructors
    public WarehouseDTO() {}
    public WarehouseDTO(Long id) {
        this.id = id;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Override
    public String toString() {
        return "WarehouseDTO{" +
                "id=" + id +
                '}';
    }
}
