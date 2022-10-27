package com.neorislab.tp2.repositorios;

import com.neorislab.tp2.entidades.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmpleadoRepositorio extends JpaRepository<Empleado, Long> {
    boolean existsByEmail(String email);


    // Se agregó esta linea que no estaba en el tp2
    boolean existsByNumeroDeDocumento(Long numeroDeDocumento);

    Optional<Empleado> findByNumeroDeDocumento(long numeroDeDocumento);
}
