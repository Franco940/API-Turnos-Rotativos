package com.neorislab.tp2.entidades;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Table(name = "empleado")
@Entity
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true) // Hace que los setters devuelvan "this" en lugar
public class Empleado {

    // TODO: Agregar unos atributos para saber si el empleado esta de vacaciones o en día libre?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) // Para que no exista un setter del id
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private Long numeroDeDocumento;

    @Column(unique = true)
    private String email;

    private boolean vacaciones;
}
