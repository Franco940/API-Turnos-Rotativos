package com.neorislab.tp2.excepciones;

public class HorasSemanalesExcedidasExcepcion extends RuntimeException{
    public HorasSemanalesExcedidasExcepcion(){
        super();
    }

    public HorasSemanalesExcedidasExcepcion(String message) {
        super(message);
    }

    public HorasSemanalesExcedidasExcepcion(String message, Throwable cause) {
        super(message, cause);
    }

    public HorasSemanalesExcedidasExcepcion(Throwable cause) {
        super(cause);
    }
}
