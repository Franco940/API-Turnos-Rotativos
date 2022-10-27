package com.neorislab.tp2.entidades;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "tipo_de_jornadas_laborales")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class TipoDeJornadaLaboral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String nombre;
}
