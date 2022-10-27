package com.neorislab.tp2.excepciones;

public class TurnoCompletoExcepcion extends RuntimeException{
    public TurnoCompletoExcepcion(){
        super();
    }

    public TurnoCompletoExcepcion(String message) {
        super(message);
    }

    public TurnoCompletoExcepcion(String message, Throwable cause) {
        super(message, cause);
    }

    public TurnoCompletoExcepcion(Throwable cause) {
        super(cause);
    }
}
