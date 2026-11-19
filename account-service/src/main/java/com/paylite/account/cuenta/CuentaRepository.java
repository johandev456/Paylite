
package com.paylite.account.cuenta;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository; 

public interface CuentaRepository extends JpaRepository<Cuenta,UUID>{
    List<Cuenta> findByOwnerId(String ownerId);
    Optional<Cuenta> findByIdAndOwnerId(UUID id, String ownerId);
    
}