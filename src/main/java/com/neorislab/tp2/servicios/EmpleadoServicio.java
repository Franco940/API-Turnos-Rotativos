package com.neorislab.tp2.servicios;

import com.neorislab.tp2.dto.EmpleadoRequest;
import com.neorislab.tp2.dto.EmpleadoResponse;
import com.neorislab.tp2.entidades.Empleado;

import java.util.Optional;

public interface EmpleadoServicio {
    Empleado buscarEmpleadoPorId(Long id) throws Exception;

    Optional<Empleado> buscarEmpleadoPorNumeroDeDocumento(Long numeroDeDocumento);

    EmpleadoResponse crearEmpleado(EmpleadoRequest empleado) throws RuntimeException;

    EmpleadoResponse cambiarEstadoVacacionesDeUnEmpleado(Long numeroDeDocumento, boolean estado);
}
