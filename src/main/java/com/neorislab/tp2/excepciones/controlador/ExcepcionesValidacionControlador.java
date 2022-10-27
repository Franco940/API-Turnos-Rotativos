package com.neorislab.tp2.excepciones.controlador;

import com.neorislab.tp2.dto.ErroresValidacionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.validation.BindException;
import java.util.stream.Collectors;

/*
    La funcionalidad de esta clase es contener los métodos que capturan las excepciones que no fueron capturadas por un bloque try-catch.
    Por ejemplo, las anotaciones de validacion de un dto, lanzan una excepcion que no es capturada y quedan "en el aire"

    Devuelve el estado HTTP correspondiente y una lista con todas las restricciones violadas de un dto
 */

@ControllerAdvice
public class ExcepcionesValidacionControlador {

    // Captura la excepción lanzada cuando se violaría una restricción del dto
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ErroresValidacionResponse violacionDeRestriccion(BindException e) {
        return new ErroresValidacionResponse(
                e.getBindingResult().getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList())
        );
    }
}
