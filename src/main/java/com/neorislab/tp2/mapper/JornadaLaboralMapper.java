package com.neorislab.tp2.mapper;

import com.neorislab.tp2.dto.JornadaLaboralResponse;
import com.neorislab.tp2.entidades.JornadaLaboral;
import org.springframework.stereotype.Component;

@Component
public class JornadaLaboralMapper {

    public JornadaLaboralResponse entidadADTOResponse(JornadaLaboral jornada, String fechaAux){
        return new JornadaLaboralResponse()
                .setId(jornada.getId())
                .setEmpleado(jornada.getEmpleado())
                .setTipoDeJornadaLaboral(jornada.getTipoDeJornada())
                .setFecha(fechaAux)
                .setHoraComienzo(jornada.getHoraComienzo())
                .setHoraFinalizacion(jornada.getHoraFinalizacion())
                .setHorasTrabajadas(jornada.getHorasTrabajadas());
    }
}
