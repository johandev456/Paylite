package com.paylite.account.cuenta;
import java.util.UUID;

import org.springframework.http.HttpStatus;
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
        @RequestHeader("X-Owner-Id") String ownerId,
        @RequestBody @Valid CrearCuentaRequest request)
    {
        
        return service.crear(ownerId, request);
    }
    
    @GetMapping("/{id}")
    public CuentaResponse obtener(
        @PathVariable("id") UUID id,
        @RequestHeader("X-Owner-Id") String ownerId){
        return service.obtener(id, ownerId);
    }
    
    @PostMapping("/{id}/debito")
    public CuentaResponse debitar(
        @PathVariable("id") UUID id,
        @RequestHeader("X-Owner-Id") String ownerId,
        @RequestBody @Valid DebitoRequest request
    ){
        return service.debitar(id,ownerId,request);
    }
}