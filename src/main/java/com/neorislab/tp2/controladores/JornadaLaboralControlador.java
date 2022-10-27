package com.neorislab.tp2.controladores;

import com.neorislab.tp2.dto.HorasPorTipoDeJornadaResponse;
import com.neorislab.tp2.dto.JornadaLaboralRequest;
import com.neorislab.tp2.dto.JornadaLaboralResponse;
import com.neorislab.tp2.dto.JornadaLaboralUpdateDTO;
import com.neorislab.tp2.entidades.JornadaLaboral;
import com.neorislab.tp2.servicios.JornadaLaboralServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.List;


@RestController()
@RequestMapping("/jornada")
public class JornadaLaboralControlador {
    @Autowired
    private JornadaLaboralServicio jornadaLaboralServicio;

    // El response entity que devuelve el método es generico para así poder devolver mensajes de error
    @CrossOrigin // Esto evita el error de CORS
    @GetMapping
    public ResponseEntity<?> getJornadaLaboralPorId(@RequestParam(required = true, name = "id") Long id){
        try{
            JornadaLaboral jornada = jornadaLaboralServicio.buscarJornadaLaboralPorId(id);

            return ResponseEntity.ok().body(jornada);
        }catch (EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getHorasPorTipoDeJornada(){
        try{
            List<HorasPorTipoDeJornadaResponse> horasPorTipoDeJornada = jornadaLaboralServicio.buscarPorCadaEmpleadoLasHorasCargadasPorTipoDeJornada();

            return ResponseEntity.ok().body(horasPorTipoDeJornada);
        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @CrossOrigin // Esto evita el error de CORS
    @PostMapping("/laboral")
    public ResponseEntity<?> crearUnaJornadaLaboral(@Valid @RequestBody JornadaLaboralRequest dtoRequest){
        try{
            JornadaLaboralResponse jornadaLaboral = jornadaLaboralServicio.crearJornadaLaboral(dtoRequest);

            return ResponseEntity.created(URI.create(String.format("/jornada/laboral/%d", jornadaLaboral.getId())))
                    .body(jornadaLaboral);

        }catch (DateTimeParseException ex){
            return ResponseEntity.badRequest().body("Hora invalida");

        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());

        }catch (ParseException ex){
            return ResponseEntity.badRequest().body("Fecha inválida");
        }
    }

    @CrossOrigin // Esto evita el error de CORS
    @PutMapping("/laboral")
    public ResponseEntity<?> actualizarHorasDeUnaJornada(@Valid @RequestBody JornadaLaboralUpdateDTO dtoRequest){
        try {
            JornadaLaboralResponse jornadaLaboral = jornadaLaboralServicio.actualizarHorasDeUnaJornada(dtoRequest);

            return ResponseEntity.ok().body(jornadaLaboral);
        }catch (DateTimeParseException ex){
            return ResponseEntity.badRequest().body("Hora invalida");
        } catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }catch (ParseException ex){
            return ResponseEntity.badRequest().body("Fecha invalida");
        }
    }
}
