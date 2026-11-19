package com.paylite.account.cuenta;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void debeDebitarYAcreditarSaldo() {
        Cuenta cuenta = new Cuenta("123456789", "USD");
        cuenta.acreditar(new BigDecimal("1000.00"));

        cuenta.debitar(new BigDecimal("250.00"));
        cuenta.acreditar(new BigDecimal("100.00"));

        assertEquals(0, new BigDecimal("850.00").compareTo(cuenta.getSaldo()));
    }

    @Test
    void ValidarCasoNull() {
        Cuenta cuenta = new Cuenta("123456789", "USD");

        
        
        assertThrows(NullPointerException.class,
        () -> cuenta.debitar(null));
    }

    @Test
    void montoNegativo(){
        Cuenta cuenta = new Cuenta("123456789", "USD");

        
        assertThrows(IllegalArgumentException.class,()-> cuenta.acreditar(new BigDecimal(-50.0)));
    }
}
