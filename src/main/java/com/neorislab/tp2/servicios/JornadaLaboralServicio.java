package com.neorislab.tp2.servicios;

import com.neorislab.tp2.dto.HorasPorTipoDeJornadaResponse;
import com.neorislab.tp2.dto.JornadaLaboralRequest;
import com.neorislab.tp2.dto.JornadaLaboralResponse;
import com.neorislab.tp2.dto.JornadaLaboralUpdateDTO;
import com.neorislab.tp2.entidades.JornadaLaboral;
import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.List;

public interface JornadaLaboralServicio {
    JornadaLaboral buscarJornadaLaboralPorId(Long id) throws EntityNotFoundException;

    JornadaLaboralResponse crearJornadaLaboral(JornadaLaboralRequest dtoRequest) throws EntityNotFoundException, ParseException,
            DateTimeParseException;

    JornadaLaboralResponse actualizarHorasDeUnaJornada(JornadaLaboralUpdateDTO dtoRequest) throws ParseException;

    List<HorasPorTipoDeJornadaResponse> buscarPorCadaEmpleadoLasHorasCargadasPorTipoDeJornada();
}
