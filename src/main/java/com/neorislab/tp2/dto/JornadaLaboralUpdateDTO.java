package com.neorislab.tp2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true) // Hace que los setters devuelvan "this" en lugar de void
public class JornadaLaboralUpdateDTO {
    // Serían el número maximos y minimo de DNI que se aceptan
    @Max(value = 999999999, message = "Numero de documento inválido. Debe de contener maximo 9 números")
    @Min(value = 1000000, message = "Numero de documento inválido. Debe de contener minimo 8 números")
    private long numeroDeDocumento;

    // El string de la fecha siempre son 10 caracteres (dd-MM-yyyy)
    @Size(min = 10, max = 10, message = "Formato de fecha invalida - Ejemplo: 01/01/1970")
    private String fechaDeLaJornada;

    @Max(value = 12, message = "Maximo 12 horas")
    @Min(value = 2, message = "Minimo 2 horas")
    private int nuevasHorasDeTrabajo;
}
