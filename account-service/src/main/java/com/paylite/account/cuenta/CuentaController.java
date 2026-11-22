package com.paylite.account.cuenta;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cuentas")
public class CuentaController{

    private final CuentaService service;

    public CuentaController(CuentaService service){
        this.service=service;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaResponse crear(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody @Valid CrearCuentaRequest request)
    {
        
        return service.crear(jwt.getSubject(), request);
    }
    
    @GetMapping("/{id}")
    public CuentaResponse obtener(
        @PathVariable("id") UUID id,
        @AuthenticationPrincipal Jwt jwt
    ){
        return service.obtener(id, jwt.getSubject());
    }

    @PostMapping("/{id}/debito")
    public CuentaResponse debitar(
        @PathVariable("id") UUID id,
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody @Valid DebitoRequest request
    ){
        return service.debitar(id,jwt.getSubject(),request);
    }
}