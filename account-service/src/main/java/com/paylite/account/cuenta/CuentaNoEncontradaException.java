
package com.paylite.account.cuenta;

public class CuentaNoEncontradaException extends RuntimeException{
    
    public CuentaNoEncontradaException(String message){
        super(message);
    }
}