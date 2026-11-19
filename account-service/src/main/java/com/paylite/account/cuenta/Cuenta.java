package com.paylite.account.cuenta;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "cuenta")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Column(nullable = false, length = 3)
    private String moneda;

    protected Cuenta() {
    }
}
