package com.paylite.account.cuenta;

import jakarta.validation.constraints.*;

public record CrearCuentaRequest(@NotBlank @Pattern(regexp = "[A-Z]{3}", message="Codigo ISO erroneo") String moneda){
    
}