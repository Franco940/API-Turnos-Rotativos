package com.neorislab.tp2.dto;

import com.neorislab.tp2.entidades.Empleado;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true) // Hace que los setters devuelvan "this" en lugar de void
public class EmpleadoResponse {
    private String mensaje;
    private Empleado empleado;
}
