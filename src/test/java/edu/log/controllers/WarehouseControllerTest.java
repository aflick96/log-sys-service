package edu.log.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.log.models.warehouse.Warehouse;
import edu.log.services.warehouse.WarehouseService;
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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WarehouseController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = edu.log.utils.TokenAuthFilter.class)
})
@Import(TestSecurityConfig.class)
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseService warehouseService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllWarehouses_success() throws Exception {
        Warehouse warehouse = new Warehouse("Warehouse A", "123 Main St, City");
        when(warehouseService.getAllWarehouses()).thenReturn(List.of(warehouse));

        mockMvc.perform(get("/api/warehouses"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllWarehouses_noContent() throws Exception {
        when(warehouseService.getAllWarehouses()).thenReturn(List.of());

        mockMvc.perform(get("/api/warehouses"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getWarehouseById_success() throws Exception {
        Warehouse warehouse = new Warehouse("Warehouse A", "123 Main St, City");
        when(warehouseService.getWarehouseById(1L)).thenReturn(warehouse);

        mockMvc.perform(get("/api/warehouses/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getWarehouseById_notFound() throws Exception {
        when(warehouseService.getWarehouseById(1L))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Warehouse not found with id: 1"));

        mockMvc.perform(get("/api/warehouses/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createWarehouse_success() throws Exception {
        Warehouse warehouse = new Warehouse("Warehouse A", "123 Main St, City");
        when(warehouseService.createWarehouse(anyString(), anyString())).thenReturn(warehouse);

        mockMvc.perform(post("/api/warehouses")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(warehouse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Warehouse A"));
    }
}
