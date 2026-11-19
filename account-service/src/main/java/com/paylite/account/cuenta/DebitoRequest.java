package com.paylite.account.cuenta;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;
public record DebitoRequest( @Positive @NotNull BigDecimal monto){}