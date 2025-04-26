package edu.log.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.log.models.shipping.ShippingStatus;
import edu.log.models.shipping.enums.ShippingStatusType;
import edu.log.models.booking.Booking;
import edu.log.models.warehouse.Warehouse;
import edu.log.services.shipping.ShippingStatusService;
import edu.log.config.TestSecurityConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShippingStatusController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = edu.log.utils.TokenAuthFilter.class)
})
@Import(TestSecurityConfig.class)
class ShippingStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShippingStatusService shippingStatusService;

    private ObjectMapper objectMapper;
    private ShippingStatus dummyShippingStatus;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Create a dummy ShippingStatus
        Booking booking = new Booking();
        Warehouse warehouse = new Warehouse();
        dummyShippingStatus = new ShippingStatus(booking, ShippingStatusType.IN_TRANSIT, warehouse);
    }

    @Test
    void getShippingStatusByBookingId_success() throws Exception {
        when(shippingStatusService.getShippingStatusByBookingId(1L)).thenReturn(dummyShippingStatus);

        mockMvc.perform(get("/api/shipping/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getShippingStatusByBookingId_notFound() throws Exception {
        when(shippingStatusService.getShippingStatusByBookingId(1L)).thenReturn(null);

        mockMvc.perform(get("/api/shipping/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getShipmentsByStatus_success() throws Exception {
        when(shippingStatusService.getShipmentsByStatus(ShippingStatusType.IN_TRANSIT))
                .thenReturn(List.of(dummyShippingStatus));

        mockMvc.perform(get("/api/shipping/status/IN_TRANSIT"))
                .andExpect(status().isOk());
    }

    @Test
    void getShipmentsByStatus_noContent() throws Exception {
        when(shippingStatusService.getShipmentsByStatus(ShippingStatusType.IN_TRANSIT))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/shipping/status/IN_TRANSIT"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createShippingStatus_success() throws Exception {
        when(shippingStatusService.createShippingStatus(1L, ShippingStatusType.IN_TRANSIT, 1L))
                .thenReturn(dummyShippingStatus);

        mockMvc.perform(post("/api/shipping")
                .param("bookingId", "1")
                .param("status", "IN_TRANSIT")
                .param("warehouseId", "1"))
                .andExpect(status().isCreated());
    }

    @Test
    void createShippingStatus_badRequest() throws Exception {
        when(shippingStatusService.createShippingStatus(1L, ShippingStatusType.IN_TRANSIT, 1L))
                .thenReturn(null);

        mockMvc.perform(post("/api/shipping")
                .param("bookingId", "1")
                .param("status", "IN_TRANSIT")
                .param("warehouseId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateShippingStatus_success() throws Exception {
        when(shippingStatusService.updateShippingStatus(1L, ShippingStatusType.DELIVERED))
                .thenReturn(dummyShippingStatus);

        mockMvc.perform(put("/api/shipping/1")
                .param("status", "DELIVERED"))
                .andExpect(status().isOk());
    }

    @Test
    void updateShippingStatus_badRequest() throws Exception {
        when(shippingStatusService.updateShippingStatus(1L, ShippingStatusType.DELIVERED))
                .thenReturn(null);

        mockMvc.perform(put("/api/shipping/1")
                .param("status", "DELIVERED"))
                .andExpect(status().isBadRequest());
    }
}
