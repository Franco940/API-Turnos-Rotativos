package com.neorislab.tp2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class JornadaLaboralRequest {
    // Serían el número maximos y minimo de DNI que se aceptan
    @Max(value = 999999999, message = "Numero de documento inválido. Debe de contener maximo 9 números")
    @Min(value = 1000000, message = "Numero de documento inválido. Debe de contener minimo 8 números")
    private long numeroDeDocumento;

    // El string de la fecha siempre son 10 caracteres (dd-MM-yyyy)
    @Size(min = 10, max = 10, message = "Formato de fecha invalida - Ejemplo: 01/01/1970")
    private String fecha;

    @Size(min = 8, max = 8, message = "Formato de hora de comienzo invalida")
    private String horaComienzo;

    @Size(min = 8, max = 8, message = "Formato de hora de finalizacion invalida")
    private String horaFinalizacion;

    private String tipoDeJornadaLaboral;
}
