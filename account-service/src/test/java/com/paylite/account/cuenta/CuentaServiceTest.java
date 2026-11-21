package com.paylite.account.cuenta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CuentaServiceTest {

    @Mock
    private CuentaRepository repository;

    private CuentaService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CuentaService(repository);
    }

    @Test
    void crearPersisteCuentaNuevaConSaldoCero() {
        when(repository.save(any(Cuenta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CuentaResponse response = service.crear("owner-1", new CrearCuentaRequest("USD"));

        assertEquals(BigDecimal.ZERO, response.saldo());
        assertEquals("USD", response.moneda());
        verify(repository).save(any(Cuenta.class));
    }

    @Test
    void obtenerCuentaDelPropietario() {
        UUID id = UUID.randomUUID();
        Cuenta cuenta = new Cuenta("owner-1", "EUR");
        when(repository.findByIdAndOwnerId(id, "owner-1")).thenReturn(Optional.of(cuenta));

        CuentaResponse response = service.obtener(id, "owner-1");

        assertEquals(BigDecimal.ZERO, response.saldo());
        assertEquals("EUR", response.moneda());
        verify(repository).findByIdAndOwnerId(id, "owner-1");
    }

    @Test
    void obtenerCuentaAjenaLanzaCuentaNoEncontrada() {
        UUID id = UUID.randomUUID();
        when(repository.findByIdAndOwnerId(id, "owner-2")).thenReturn(Optional.empty());

        assertThrows(CuentaNoEncontradaException.class,
                () -> service.obtener(id, "owner-2"));
    }

    @Test
    void debitarDescontaSaldoYPersiste() {
        UUID id = UUID.randomUUID();
        Cuenta cuenta = new Cuenta("owner-1", "USD");
        cuenta.acreditar(new BigDecimal("500"));
        when(repository.findByIdAndOwnerId(id, "owner-1")).thenReturn(Optional.of(cuenta));
        when(repository.save(cuenta)).thenReturn(cuenta);

        CuentaResponse response = service.debitar(id, "owner-1", new DebitoRequest(new BigDecimal("200")));

        assertEquals(0, new BigDecimal("300").compareTo(response.saldo()));
        verify(repository).save(cuenta);
    }

    @Test
    void debitarSinSaldoSuficienteNoPersiste() {
        UUID id = UUID.randomUUID();
        Cuenta cuenta = new Cuenta("owner-1", "USD");
        when(repository.findByIdAndOwnerId(id, "owner-1")).thenReturn(Optional.of(cuenta));

        assertThrows(SaldoInsuficienteException.class,
                () -> service.debitar(id, "owner-1", new DebitoRequest(new BigDecimal("1"))));
    }
}
