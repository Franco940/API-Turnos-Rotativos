package com.neorislab.tp2.servicios;

import com.neorislab.tp2.dto.TipoDeJornadaLaboralDTO;
import com.neorislab.tp2.entidades.TipoDeJornadaLaboral;
import java.util.Optional;

public interface TipoDeJornadaLaboralServicio {
    TipoDeJornadaLaboral crearTipoDeJornada(TipoDeJornadaLaboralDTO request) throws Exception;

    Optional<TipoDeJornadaLaboral> buscarJornadaLaboralPorNombre(String nombreDeLaJornada);

    long obtenerElIdDelDiaLibre();
}
