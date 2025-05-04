/*
 * Warehouse.java
 * 
 * This class represents a Warehouse entity in the system. It is mapped to the 'warehouse' table in the database.
 */

package edu.log.models.warehouse;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "warehouse")
public class Warehouse {
    
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Warehouse name must not be empty")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Warehouse address must not be empty")
    @Size(min = 10, message = "Warehouse address must be at least 10 characters long")
    @Column(name = "address")
    private String address;

    // Constructors
    public Warehouse() {}
    public Warehouse(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
