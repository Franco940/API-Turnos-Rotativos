package com.neorislab.tp2.dto;

import com.neorislab.tp2.entidades.Empleado;
import com.neorislab.tp2.entidades.TipoDeJornadaLaboral;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true) // Hace que los setters devuelvan "this" en lugar de void
public class JornadaLaboralResponse {
    private long id;
    private String fecha;
    private LocalTime horaComienzo;
    private LocalTime horaFinalizacion;
    private int horasTrabajadas;
    private Empleado empleado;
    private TipoDeJornadaLaboral tipoDeJornadaLaboral;
}
