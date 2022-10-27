package com.neorislab.tp2.entidades;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;

@Table(name = "jornada_laboral")
@Entity
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true) // Hace que los setters devuelvan "this"
public class JornadaLaboral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) // Para que no exista un setter del id
    private long id;

    // Se usa la anotación "DateTimeFormat" para elegir la forma en la que se quiere guardar la fecha
    // ya que una fecha entera contiene día-mes-año hora:minutos:segundos
    @DateTimeFormat(pattern = "dd/MM/yyyy") // En este caso solo guardamos la fecha
    @Temporal(TemporalType.DATE)
    private Date fecha;

    private LocalTime horaComienzo;
    private LocalTime horaFinalizacion;
    private Integer horasTrabajadas;

    @ManyToOne
    private Empleado empleado;

    @ManyToOne
    private TipoDeJornadaLaboral tipoDeJornada;
}
