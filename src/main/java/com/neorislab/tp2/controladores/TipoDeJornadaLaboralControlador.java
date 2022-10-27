package com.neorislab.tp2.controladores;

import com.neorislab.tp2.dto.TipoDeJornadaLaboralDTO;
import com.neorislab.tp2.entidades.TipoDeJornadaLaboral;
import com.neorislab.tp2.servicios.TipoDeJornadaLaboralServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/jornada/crear")
public class TipoDeJornadaLaboralControlador {
    @Autowired
    private TipoDeJornadaLaboralServicio tipoDeJornadaLaboralServicio;


    @PostMapping
    public ResponseEntity<?> crearTipoDeJornada(@Valid @RequestBody TipoDeJornadaLaboralDTO dtoRequest){
        try{
            TipoDeJornadaLaboral tipoDeJornadaLaboral = tipoDeJornadaLaboralServicio.crearTipoDeJornada(dtoRequest);

            return ResponseEntity.created(URI.create(String.format("/jornada/crear/%d", tipoDeJornadaLaboral.getId())))
                    .body(tipoDeJornadaLaboral);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }
}
