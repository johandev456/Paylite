package com.paylite.account;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.paylite.account.cuenta.CuentaNoEncontradaException;
import com.paylite.account.cuenta.SaldoInsuficienteException;



@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CuentaNoEncontradaException.class)
    public ProblemDetail manejarNoEncontrada(CuentaNoEncontradaException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ProblemDetail manejarSaldo(SaldoInsuficienteException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail manejarArgumento(IllegalArgumentException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
