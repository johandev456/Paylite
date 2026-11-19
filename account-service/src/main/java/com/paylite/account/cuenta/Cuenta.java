package com.paylite.account.cuenta;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
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

    @Column(nullable = false)
    private Instant createdAt;

    

    protected Cuenta() {
    } //Este se usa para que hibernate pueda usarlo 
    public Cuenta(String ownerId, String moneda){
        // Este se usa para crear una cuenta desde 0
        this.ownerId=ownerId;
        this.moneda=moneda;
        this.saldo=BigDecimal.ZERO;
        this.createdAt=(Instant.now());
    }

    private void validarMonto(BigDecimal monto) {
        Objects.requireNonNull(monto,"El monto no puede ser nulo");
        
        if(monto.compareTo(BigDecimal.ZERO) <= 0){
            	throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
    }

    public void acreditar(BigDecimal monto){
            validarMonto(monto);
            this.saldo=this.saldo.add(monto);
        
    }

    public void debitar(BigDecimal monto){
        validarMonto(monto);
        if(saldo.compareTo(monto) < 0){
            	throw new SaldoInsuficienteException("Saldo insuficiente para debitar el monto especificado");
        }
               
            this.saldo=this.saldo.subtract(monto);
        
    }

    public UUID getId() {
        return id;
    }


    public String getOwnerId() {
        return ownerId;
    }


    public BigDecimal getSaldo() {
        return saldo;
    }

    public String getMoneda() {
        return moneda;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    
}
