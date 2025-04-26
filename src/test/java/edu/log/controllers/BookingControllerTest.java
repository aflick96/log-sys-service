package edu.log.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.log.dto.BookingRequestDTO;
import edu.log.models.booking.Booking;
import edu.log.models.booking.enums.BookingServiceType;
import edu.log.models.warehouse.Warehouse;
import edu.log.services.booking.BookingService;
import edu.log.services.warehouse.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import edu.log.config.TestSecurityConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;


@WebMvcTest(controllers = BookingController.class, excludeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = edu.log.utils.TokenAuthFilter.class)
})
@Import(TestSecurityConfig.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private WarehouseService warehouseService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllBookings_success() throws Exception {
        Booking booking = new Booking();
        when(bookingService.getAllBookings()).thenReturn(List.of(booking));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingById_success() throws Exception {
        Booking booking = new Booking();
        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking));

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingById_notFound() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBooking_success() throws Exception {
        BookingRequestDTO requestDTO = new BookingRequestDTO();
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        requestDTO.setWarehouse(new edu.log.dto.WarehouseDTO(1L));
        requestDTO.setContainerId("CONT-123456");
        requestDTO.setToAddress("123 Main Street, City");
        requestDTO.setDescription("Some stuff");
        requestDTO.setServiceType("ECONOMY");
        requestDTO.setVolume(20.0);
        requestDTO.setWeight(10.0);

        Booking createdBooking = new Booking();
        createdBooking.setContainerId("CONT-123456");

        when(bookingService.getBookingByContainerId("CONT-123456")).thenReturn(Optional.empty());
        when(warehouseService.getWarehouseById(1L)).thenReturn(warehouse);
        when(bookingService.createBooking(any(Booking.class))).thenReturn(createdBooking);

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.containerId").value("CONT-123456"));
    }

    @Test
    void createBooking_duplicateContainerId_conflict() throws Exception {
        BookingRequestDTO requestDTO = new BookingRequestDTO();
        requestDTO.setWarehouse(new edu.log.dto.WarehouseDTO(1L));
        requestDTO.setContainerId("CONT-123456");

        when(bookingService.getBookingByContainerId("CONT-123456")).thenReturn(Optional.of(new Booking()));

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteAllBookings_success() throws Exception {
        doNothing().when(bookingService).deleteAllBookings();

        mockMvc.perform(delete("/api/bookings"))
                .andExpect(status().isNoContent());
    }
}
