package com.neorislab.tp2.mapper;

import com.neorislab.tp2.dto.TipoDeJornadaLaboralDTO;
import com.neorislab.tp2.entidades.TipoDeJornadaLaboral;
import org.springframework.stereotype.Component;

@Component
public class TipoDeJornadaLaboralMapper {

    public TipoDeJornadaLaboral dtoAEntidad(TipoDeJornadaLaboralDTO dto){
        TipoDeJornadaLaboral tipoJornada = new TipoDeJornadaLaboral();
        tipoJornada.setNombre(dto.getNombre());

        return tipoJornada;
    }
}
