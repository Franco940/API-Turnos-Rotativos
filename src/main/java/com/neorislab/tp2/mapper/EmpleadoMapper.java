package com.neorislab.tp2.mapper;

import com.neorislab.tp2.dto.EmpleadoRequest;
import com.neorislab.tp2.dto.EmpleadoResponse;
import com.neorislab.tp2.entidades.Empleado;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoMapper {

    public Empleado empleadoDTOAEntidad(EmpleadoRequest er){
        return new Empleado()
                .setNombre(er.getNombre())
                .setApellido(er.getApellido())
                .setNumeroDeDocumento(er.getNumeroDeDocumento())
                .setEmail(er.getEmail())
                .setVacaciones(false);
    }

    public EmpleadoResponse empleadoEntidadADTOResponse(Empleado em, String mensaje){
        return new EmpleadoResponse()
                .setMensaje(mensaje)
                .setEmpleado(em);
    }
}
