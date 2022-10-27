package com.neorislab.tp2.repositorios;

import com.neorislab.tp2.entidades.TipoDeJornadaLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TipoDeJornadaLaboralRepositorio extends JpaRepository<TipoDeJornadaLaboral, Long> {
    boolean existsByNombre(String nombre);

    Optional<TipoDeJornadaLaboral> findByNombre(String nombre);
}
