package com.neorislab.tp2.excepciones;

public class EmpleadoYaRegistradoExcepcion extends RuntimeException{
    public EmpleadoYaRegistradoExcepcion(){
        super();
    }

    public EmpleadoYaRegistradoExcepcion(String message) {
        super(message);
    }

    public EmpleadoYaRegistradoExcepcion(String message, Throwable cause) {
        super(message, cause);
    }

    public EmpleadoYaRegistradoExcepcion(Throwable cause) {
        super(cause);
    }
}
