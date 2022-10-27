package com.neorislab.tp2.excepciones;

public class EmpleadoNoPuedeTrabajarExcepcion extends RuntimeException{
    public EmpleadoNoPuedeTrabajarExcepcion(){
        super();
    }

    public EmpleadoNoPuedeTrabajarExcepcion(String message) {
        super(message);
    }

    public EmpleadoNoPuedeTrabajarExcepcion(String message, Throwable cause) {
        super(message, cause);
    }

    public EmpleadoNoPuedeTrabajarExcepcion(Throwable cause) {
        super(cause);
    }
}
