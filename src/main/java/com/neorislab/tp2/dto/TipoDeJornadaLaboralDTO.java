package com.neorislab.tp2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
public class TipoDeJornadaLaboralDTO {
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;
}
