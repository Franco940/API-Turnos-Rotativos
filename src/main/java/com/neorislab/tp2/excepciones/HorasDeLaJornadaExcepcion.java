package com.neorislab.tp2.excepciones;

public class HorasDeLaJornadaExcepcion extends RuntimeException{
    public HorasDeLaJornadaExcepcion(){
        super();
    }

    public HorasDeLaJornadaExcepcion(String message) {
        super(message);
    }

    public HorasDeLaJornadaExcepcion(String message, Throwable cause) {
        super(message, cause);
    }

    public HorasDeLaJornadaExcepcion(Throwable cause) {
        super(cause);
    }
}
