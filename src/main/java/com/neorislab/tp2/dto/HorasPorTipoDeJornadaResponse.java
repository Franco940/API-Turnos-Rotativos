package com.neorislab.tp2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true) // Hace que los setters devuelvan "this" en lugar de void
public class HorasPorTipoDeJornadaResponse {
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private long numeroDeDocumento;
    private String tipoJornada;
    private int horasTrabajadas;
}
