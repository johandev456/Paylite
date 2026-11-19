
package com.paylite.account.cuenta;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CuentaService{
private final CuentaRepository repository;

private Cuenta buscarCuenta(UUID id, String ownerId) {
   return repository.findByIdAndOwnerId(id, ownerId).orElseThrow(()-> new CuentaNoEncontradaException("Cuenta no encontrada"));
}

public CuentaService(CuentaRepository repository){

    this.repository=repository;
}
    @Transactional
    public CuentaResponse crear(String ownerId, CrearCuentaRequest request){
        Cuenta nuevaCuenta =new Cuenta(ownerId, request.moneda());
        return CuentaResponse.from(repository.save(nuevaCuenta));


    }
    @Transactional(readOnly = true)
    public CuentaResponse obtener(UUID id, String ownerId){
        Cuenta cuenta = buscarCuenta(id, ownerId);
        
        return CuentaResponse.from(cuenta);
    }

    @Transactional
    public CuentaResponse debitar(UUID id, String ownerId, DebitoRequest request){
        Cuenta cuenta = buscarCuenta(id, ownerId);
        cuenta.debitar(request.monto());
        repository.save(cuenta);
        return CuentaResponse.from(cuenta);
    }


}