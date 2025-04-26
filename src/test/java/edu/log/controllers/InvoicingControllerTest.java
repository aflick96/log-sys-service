package edu.log.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.log.dto.InvoiceDTO;
import edu.log.models.invoicing.Invoice;
import edu.log.models.invoicing.enums.InvoiceStatus;
import edu.log.services.invoicing.InvoiceService;
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
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InvoicingController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = edu.log.utils.TokenAuthFilter.class)
})
@Import(TestSecurityConfig.class)
class InvoicingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllInvoices_success() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setTotalAmount(100.0);
        invoice.setStatus(InvoiceStatus.UNPAID);

        when(invoiceService.getAllInvoices()).thenReturn(List.of(invoice));

        mockMvc.perform(get("/api/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalAmount").value(100.0))
                .andExpect(jsonPath("$[0].status").value("UNPAID"));
    }

    @Test
    void getAllInvoices_noContent() throws Exception {
        when(invoiceService.getAllInvoices()).thenReturn(List.of());

        mockMvc.perform(get("/api/invoices"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUnpaidInvoices_success() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setTotalAmount(100.0);
        invoice.setStatus(InvoiceStatus.UNPAID);

        when(invoiceService.getUnpaidInvoices()).thenReturn(Optional.of(List.of(invoice)));

        mockMvc.perform(get("/api/invoices/unpaid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("UNPAID"));
    }

    @Test
    void getUnpaidInvoices_noContent() throws Exception {
        when(invoiceService.getUnpaidInvoices()).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/invoices/unpaid"))
                .andExpect(status().isNoContent());
    }

    @Test
    void payInvoice_success() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setTotalAmount(100.0);
        invoice.setStatus(InvoiceStatus.PAID);

        when(invoiceService.markAsPaid(1L)).thenReturn(invoice);

        mockMvc.perform(put("/api/invoices/1/pay"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    void payInvoice_notFound() throws Exception {
        when(invoiceService.markAsPaid(1L))
                .thenThrow(new IllegalArgumentException("Invoice not found"));

        mockMvc.perform(put("/api/invoices/1/pay"))
                .andExpect(status().isNotFound());
    }

    @Test
    void payInvoice_alreadyPaid() throws Exception {
        when(invoiceService.markAsPaid(1L))
                .thenThrow(new IllegalStateException("Invoice is already paid"));

        mockMvc.perform(put("/api/invoices/1/pay"))
                .andExpect(status().isBadRequest());
    }
}
