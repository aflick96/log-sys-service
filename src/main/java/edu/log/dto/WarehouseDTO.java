/*
 * WarehouseDTO.java
 * 
 * This DTO represents a warehouse object with only the ID field. It is used in BookingRequestDTO to transfer warehouse information from the client to the server.
 */

package edu.log.dto;

public class WarehouseDTO {
    private Long id;

    public WarehouseDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
