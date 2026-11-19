package com.paylite.account.cuenta;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CuentaResponse(UUID id, BigDecimal saldo, String moneda, Instant createdAt)
{
    public static CuentaResponse from(Cuenta cuenta) {
        
    return new CuentaResponse(cuenta.getId(), cuenta.getSaldo(),cuenta.getMoneda(), cuenta.getCreatedAt());
    }
}