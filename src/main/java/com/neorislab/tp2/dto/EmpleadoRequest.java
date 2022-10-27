package com.neorislab.tp2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
@Setter
public class EmpleadoRequest {
    // La anotación @NotBlank corrobora que los atributos no estén vacios a la hora de mapear el BodyRequest.
    // Si no se logra mapear bien se envia el mensaje que se indica

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    // Serían el número maximos y minimo de DNI que se aceptan
    @Max(value = 999999999, message = "Numero de documento inválido. Debe de contener maximo 9 números")
    @Min(value = 1000000, message = "Numero de documento inválido. Debe de contener minimo 8 números")
    private Long numeroDeDocumento;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Formato de email incorrecto")
    private String email;
}
