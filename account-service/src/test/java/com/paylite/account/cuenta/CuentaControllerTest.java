package com.paylite.account.cuenta;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.paylite.account.GlobalExceptionHandler;

class CuentaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CuentaService service;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CuentaController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void crearCuentaDevuelve201() throws Exception {
        UUID id = UUID.randomUUID();
        Instant createdAt = Instant.parse("2026-01-15T10:00:00Z");
        when(service.crear("owner-1", new CrearCuentaRequest("USD")))
                .thenReturn(new CuentaResponse(id, BigDecimal.ZERO, "USD", createdAt));

        mockMvc.perform(post("/api/cuentas")
                        .header("X-Owner-Id", "owner-1")
                        .contentType("application/json")
                        .content("{\"moneda\":\"USD\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.saldo").value(0))
                .andExpect(jsonPath("$.moneda").value("USD"));

        verify(service).crear(eq("owner-1"), any(CrearCuentaRequest.class));
    }

    @Test
    void crearCuentaRechazaMonedaInvalida() throws Exception {
        mockMvc.perform(post("/api/cuentas")
                        .header("X-Owner-Id", "owner-1")
                        .contentType("application/json")
                        .content("{\"moneda\":\"US\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerCuentaDevuelve200() throws Exception {
        UUID id = UUID.randomUUID();
        CuentaResponse response = new CuentaResponse(
                id, new BigDecimal("125.50"), "EUR", Instant.parse("2026-01-15T10:00:00Z"));
        when(service.obtener(id, "owner-1")).thenReturn(response);

        mockMvc.perform(get("/api/cuentas/{id}", id)
                        .header("X-Owner-Id", "owner-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.saldo").value(125.50))
                .andExpect(jsonPath("$.moneda").value("EUR"));
    }

    @Test
    void obtenerCuentaNoEncontradaDevuelve404() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.obtener(id, "owner-1"))
                .thenThrow(new CuentaNoEncontradaException("Cuenta no encontrada"));

        mockMvc.perform(get("/api/cuentas/{id}", id)
                        .header("X-Owner-Id", "owner-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Cuenta no encontrada"));
    }

    @Test
    void debitarCuentaDevuelve200() throws Exception {
        UUID id = UUID.randomUUID();
        CuentaResponse response = new CuentaResponse(
                id, new BigDecimal("750.00"), "USD", Instant.parse("2026-01-15T10:00:00Z"));
        when(service.debitar(eq(id), eq("owner-1"), any(DebitoRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/cuentas/{id}/debito", id)
                        .header("X-Owner-Id", "owner-1")
                        .contentType("application/json")
                        .content("{\"monto\":250.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(750.00));
    }

    @Test
    void debitarConSaldoInsuficienteDevuelve422() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.debitar(eq(id), eq("owner-1"), any(DebitoRequest.class)))
                .thenThrow(new SaldoInsuficienteException("Saldo insuficiente"));

        mockMvc.perform(post("/api/cuentas/{id}/debito", id)
                        .header("X-Owner-Id", "owner-1")
                        .contentType("application/json")
                        .content("{\"monto\":100.00}"))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.detail").value("Saldo insuficiente"));
    }

    @Test
    void debitarConMontoNoPositivoDevuelve400() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/api/cuentas/{id}/debito", id)
                        .header("X-Owner-Id", "owner-1")
                        .contentType("application/json")
                        .content("{\"monto\":-10.00}"))
                .andExpect(status().isBadRequest());
    }
}
