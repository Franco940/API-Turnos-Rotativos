package com.neorislab.tp2.controladores;

import com.neorislab.tp2.dto.EmpleadoRequest;
import com.neorislab.tp2.dto.EmpleadoResponse;
import com.neorislab.tp2.entidades.Empleado;
import com.neorislab.tp2.excepciones.EmpleadoYaRegistradoExcepcion;
import com.neorislab.tp2.servicios.EmpleadoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/empleado")
public class EmpleadoControlador {
    @Autowired
    public EmpleadoServicio empleadoServicio;


    // El response entity que devuelve el método es generico para así poder devolver mensajes de error
    @CrossOrigin // Esto evita el error de CORS
    @GetMapping
    public ResponseEntity<?> getEmpleadoPorId(@RequestParam(required = true, name = "id") Long id){
        try{
            Empleado empleado = empleadoServicio.buscarEmpleadoPorId(id);

            return ResponseEntity.ok().body(empleado);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Crea un empleado y devuelve sus datos
    @CrossOrigin // Esto evita el error de CORS
    @PostMapping
    public ResponseEntity<?> crearEmpleado(@Valid @RequestBody EmpleadoRequest empleadoRequest){
        try{
            EmpleadoResponse empleadoResponse = empleadoServicio.crearEmpleado(empleadoRequest);

            return ResponseEntity.created(URI.create(String.format("/empleado/%d", empleadoResponse.getEmpleado().getId())))
                    .body(empleadoResponse);
        }catch (EmpleadoYaRegistradoExcepcion ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    @PutMapping("/vacaciones")
    public ResponseEntity<?> actualizarEstadoVacaciones(@RequestParam(required = true, name = "numeroDeDocumento") Long numeroDeDocumento,
                                                        @RequestParam(required = true, name = "estadoVacaciones") boolean estado){
        try {
            EmpleadoResponse empleadoResponse = empleadoServicio.cambiarEstadoVacacionesDeUnEmpleado(numeroDeDocumento, estado);

            return ResponseEntity.ok().body(empleadoResponse);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
