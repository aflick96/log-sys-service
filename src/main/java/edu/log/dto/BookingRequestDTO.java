package edu.log.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Booking request object")
public class BookingRequestDTO {

    @Schema(description = "Warehouse object with only ID", example = "{\"id\": 1}")
    private WarehouseDTO warehouse;

    @Schema(example = "Cleveland, OH")
    private String toAddress;

    @Schema(example = "Stuff")
    private String description;

    @Schema(example = "ECONOMY", allowableValues = {"ECONOMY", "EXPRESS", "OVERNIGHT"})
    private String serviceType;

    @Schema(example = "25.0")
    private Double volume;

    @Schema(example = "15.0")
    private Double weight;

    public BookingRequestDTO() {}

    public WarehouseDTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseDTO warehouse) {
        this.warehouse = warehouse;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
