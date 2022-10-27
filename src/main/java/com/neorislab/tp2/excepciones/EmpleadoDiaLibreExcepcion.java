package com.neorislab.tp2.excepciones;

public class EmpleadoDiaLibreExcepcion extends RuntimeException{
    public EmpleadoDiaLibreExcepcion(){
        super();
    }

    public EmpleadoDiaLibreExcepcion(String message) {
        super(message);
    }

    public EmpleadoDiaLibreExcepcion(String message, Throwable cause) {
        super(message, cause);
    }

    public EmpleadoDiaLibreExcepcion(Throwable cause) {
        super(cause);
    }
}
