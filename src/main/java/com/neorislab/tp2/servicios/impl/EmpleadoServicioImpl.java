package com.neorislab.tp2.servicios.impl;

import com.neorislab.tp2.dto.EmpleadoRequest;
import com.neorislab.tp2.dto.EmpleadoResponse;
import com.neorislab.tp2.entidades.Empleado;
import com.neorislab.tp2.excepciones.EmpleadoYaRegistradoExcepcion;
import com.neorislab.tp2.mapper.EmpleadoMapper;
import com.neorislab.tp2.repositorios.EmpleadoRepositorio;
import com.neorislab.tp2.servicios.EmpleadoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@Service
public class EmpleadoServicioImpl implements EmpleadoServicio {
    @Autowired
    private EmpleadoRepositorio empleadoRepositorio;

    @Autowired
    private EmpleadoMapper empleadoMapper;

    @Override
    public Empleado buscarEmpleadoPorId(Long id) throws Exception {
        boolean empleadoExiste = empleadoRepositorio.existsById(id);

        if(!empleadoExiste){
            throw new Exception("No se encontro un empleado con el id provisto");
        }

        return empleadoRepositorio.findById(id).get();
    }

    @Override
    public Optional<Empleado> buscarEmpleadoPorNumeroDeDocumento(Long numeroDeDocumento) {
        return empleadoRepositorio.findByNumeroDeDocumento(numeroDeDocumento);
    }

    @Override
    public EmpleadoResponse crearEmpleado(EmpleadoRequest empleado) throws RuntimeException {
        boolean empleadoExisteConEmail = empleadoRepositorio.existsByEmail(empleado.getEmail());

        boolean empleadoExisteConDocumento = empleadoRepositorio.existsByNumeroDeDocumento(empleado.getNumeroDeDocumento());

        if(empleadoExisteConEmail || empleadoExisteConDocumento){
            throw new EmpleadoYaRegistradoExcepcion("El email ya está registrado");
        }

        Empleado empleadoGuardado = empleadoRepositorio.save(empleadoMapper.empleadoDTOAEntidad(empleado));

        return empleadoMapper.empleadoEntidadADTOResponse(empleadoGuardado, "Empleado creado correctamente");
    }

    @Override
    public EmpleadoResponse cambiarEstadoVacacionesDeUnEmpleado(Long numeroDeDocumento, boolean estado) {
        Empleado empleadoEncontrado = empleadoRepositorio.findByNumeroDeDocumento(numeroDeDocumento)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado un empleado con el DNI provisto"));;

        empleadoEncontrado.setVacaciones(estado);

        // Si uso el .save con un registro existente, se hace actualiza el registro
        empleadoRepositorio.save(empleadoEncontrado);

        // Operadores ternarios para saber que mensaje enviar
        String mensaje = estado ? "Empleado de vacaciones" : "Empleado de vuelta al trabajo";

        return empleadoMapper.empleadoEntidadADTOResponse(empleadoEncontrado, mensaje);
    }
}
