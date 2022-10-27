package com.neorislab.tp2.servicios.impl;

import com.neorislab.tp2.dto.TipoDeJornadaLaboralDTO;
import com.neorislab.tp2.entidades.TipoDeJornadaLaboral;
import com.neorislab.tp2.mapper.TipoDeJornadaLaboralMapper;
import com.neorislab.tp2.repositorios.TipoDeJornadaLaboralRepositorio;
import com.neorislab.tp2.servicios.TipoDeJornadaLaboralServicio;
import com.neorislab.tp2.utilidad.ConstantesGlobales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TipoDeJornadaLaboralImpl implements TipoDeJornadaLaboralServicio {
    @Autowired
    private TipoDeJornadaLaboralRepositorio tipoDeJornadaLaboralRepositorio;

    @Autowired
    private TipoDeJornadaLaboralMapper tipoDeJornadaLaboralMapper;


    @Override
    public TipoDeJornadaLaboral crearTipoDeJornada(TipoDeJornadaLaboralDTO request) throws Exception {
        boolean tipoDeJornadaExiste = tipoDeJornadaLaboralRepositorio.existsByNombre(request.getNombre());

        if(tipoDeJornadaExiste){
            throw new Exception("Ya existe una jornada laboral con ese nombre");
        }

        return tipoDeJornadaLaboralRepositorio.save(tipoDeJornadaLaboralMapper.dtoAEntidad(request));
    }

    @Override
    public Optional<TipoDeJornadaLaboral> buscarJornadaLaboralPorNombre(String nombreDeLaJornada) {
        return tipoDeJornadaLaboralRepositorio.findByNombre(nombreDeLaJornada);
    }

    @Override
    public long obtenerElIdDelDiaLibre() {
        return tipoDeJornadaLaboralRepositorio.findByNombre(ConstantesGlobales.TIPOS_DE_JORNADAS.DIA_LIBRE).get().getId();
    }
}
