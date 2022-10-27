package com.neorislab.tp2.repositorios;

import com.neorislab.tp2.entidades.JornadaLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface JornadaLaboralRepositorio extends JpaRepository<JornadaLaboral, Long> {
    // Devuelve todas las jornadas de una semana del empleado
    @Query("SELECT j FROM JornadaLaboral AS j WHERE j.fecha BETWEEN :fechaSieteDiasAtras " +
            "AND :fechaActual AND empleado.id = :idEmpleado")
    Optional<List<JornadaLaboral>> buscarLasJornadasDeUnaSemanaDeUnEmpleado(@Param("idEmpleado")Long idEmpleado,
                                                                            @Param("fechaActual")Date fechaActual,
                                                                            @Param("fechaSieteDiasAtras")Date fechaSieteDiasAtras);


    // Devuelve un true o un false si existe una jornada de un empleado en un dia
    boolean existsByEmpleadoIdAndTipoDeJornadaIdAndFecha(Long empleadoId, Long tipoDeJornadaId, Date fecha);


    // Devuelve la cantiadd de dias libres que hubo en los ultimos 7 dias de un empleado
    @Query("SELECT COUNT(j) FROM JornadaLaboral AS j WHERE empleado.id = :idEmpleado AND tipoDeJornada.id = :idTipoDeJornada " +
            "AND j.fecha BETWEEN :fechaSieteDiasAtras AND :fechaActual")
    int cantidadDeDiasLibresEnLosUltimosSieteDias(@Param("idEmpleado") Long idEmpleado,
                                                  @Param("idTipoDeJornada") Long idTipoDeJornada,
                                                  @Param("fechaActual") Date fechaActual,
                                                  @Param("fechaSieteDiasAtras") Date fechaSieteDiasAtras);


    // Cuenta la cantidad de empleados que hay en una jornada del mismo turno
    int countByTipoDeJornadaIdAndFecha(Long tipoDeJornadaId, Date fecha);


    // Devuelve la jornada de un empleado
    Optional<JornadaLaboral> findByEmpleadoNumeroDeDocumentoAndFecha(Long numeroDeDocumento, Date fecha);
}
