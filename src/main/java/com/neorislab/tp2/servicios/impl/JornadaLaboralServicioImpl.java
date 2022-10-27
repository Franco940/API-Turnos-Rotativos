package com.neorislab.tp2.servicios.impl;

import com.neorislab.tp2.dto.HorasPorTipoDeJornadaResponse;
import com.neorislab.tp2.dto.JornadaLaboralRequest;
import com.neorislab.tp2.dto.JornadaLaboralResponse;
import com.neorislab.tp2.dto.JornadaLaboralUpdateDTO;
import com.neorislab.tp2.entidades.Empleado;
import com.neorislab.tp2.entidades.JornadaLaboral;
import com.neorislab.tp2.entidades.TipoDeJornadaLaboral;
import com.neorislab.tp2.excepciones.*;
import com.neorislab.tp2.mapper.JornadaLaboralMapper;
import com.neorislab.tp2.repositorios.JornadaLaboralRepositorio;
import com.neorislab.tp2.servicios.EmpleadoServicio;
import com.neorislab.tp2.servicios.JornadaLaboralServicio;
import com.neorislab.tp2.servicios.TipoDeJornadaLaboralServicio;
import com.neorislab.tp2.utilidad.ConstantesGlobales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class JornadaLaboralServicioImpl implements JornadaLaboralServicio {
    @Autowired
    private JornadaLaboralRepositorio jornadaLaboralRepositorio;

    @Autowired
    private TipoDeJornadaLaboralServicio tipoDeJornadaLaboralServicio;

    @Autowired
    private EmpleadoServicio empleadoServicio;

    @Autowired
    private JornadaLaboralMapper jornadaLaboralMapper;


    @Override
    public JornadaLaboral buscarJornadaLaboralPorId(Long id) throws EntityNotFoundException {
        return jornadaLaboralRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró una jornada laboral con el id provisto"));
    }

    @Override
    public JornadaLaboralResponse crearJornadaLaboral(JornadaLaboralRequest dtoRequest) throws EntityNotFoundException, ParseException,
            DateTimeParseException{

        // Si no se encuentra una entidad con el DNI del empleado o con el nombre del tipo de jornada se lanza una excepcion
        Empleado empleado = empleadoServicio.buscarEmpleadoPorNumeroDeDocumento(dtoRequest.getNumeroDeDocumento())
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado un empleado con el numero de DNI provisto"));

        // Si el empleado esta de vacaciones, se lanza una excepcion que es capturada en el controlador
        saberSiElEmpleadoEstaDeVacaciones(empleado);

        TipoDeJornadaLaboral tipoDeJornada = tipoDeJornadaLaboralServicio.buscarJornadaLaboralPorNombre(dtoRequest.getTipoDeJornadaLaboral())
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado el tipo de jornada de trabajo"));

        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        dateFormater.setLenient(false);

        // Corroboro el formato de la fecha
        // Esto tira una excepcion si el formato de la fecha está mal. Si se lanza la capturo en el controlador
        Date fechaDeLaJornada = dateFormater.parse(dtoRequest.getFecha());

        // Corroboro que haya lugar en el turno (si hay menos de 2 personas)
        hayLugarEnElTurno(tipoDeJornada.getId(), fechaDeLaJornada, tipoDeJornada.getNombre());

        //Esto corrobora si ya existe el mismo empleado en el mismo día con el mismo turno
        existeMismoEmpleadoEnElMismoDiaYJornada(empleado.getId(), tipoDeJornada.getId(), fechaDeLaJornada);

        // Corroboro si esta en dia libre. Si lo está no se crea una jornada
        // Se busca si existe una jornada con el id del empleado, con el id de la jornada de dia libre
        // y con el dia en el que se quiere crear una jornada nueva
        saberSiElEmpleadoEstaEnDiaLibre(empleado.getId(), fechaDeLaJornada);

        // Si no puede convertir la hora al tipo de dato LocalTime, lanza una excepcion que la capturo en el controlador
        LocalTime horaComienzo = LocalTime.parse(dtoRequest.getHoraComienzo());
        LocalTime horaFinalizacion = asignarHoraFinalizacionCorrespondiente(tipoDeJornada.getNombre(), empleado.getId(), fechaDeLaJornada,
                dtoRequest.getHoraFinalizacion());

        // Uso operador ternario para asignar las horas. Si es día libre, asigno 0
        int horasDeTrabajoEnLaJornada = tipoDeJornada.getNombre().equalsIgnoreCase(ConstantesGlobales.TIPOS_DE_JORNADAS.DIA_LIBRE) ?
                0 : calcularHoras(horaComienzo.getHour(), horaFinalizacion.getHour());

        // Corroboro que el empleado no tenga más de 48 horas trabajadas en la semana
        // Si la horasDeTrabajoEnLaJornada es 0, significa que es día libre, entonces no hacemos el calculo
        if(horasDeTrabajoEnLaJornada != 0) empleadoExcedeHorasSemanales(empleado.getId(), horasDeTrabajoEnLaJornada, dateFormater.parse(dtoRequest.getFecha()));

        // Corroboro que la jornada sea de 6 a 8 horas si es turno normal, 2 a 6 horas si es turno extra y que no sean mas de 12 si es combinado
        // Si no se cumple una condicion, se tira una excepcion que se atrapa en el controlador
        corroborarTipoDeJornadaConSusHoras(tipoDeJornada.getNombre(), horasDeTrabajoEnLaJornada);

        JornadaLaboral jornada = new JornadaLaboral()
                .setEmpleado(empleado)
                .setTipoDeJornada(tipoDeJornada)
                .setFecha(fechaDeLaJornada)
                .setHoraComienzo(horaComienzo)
                .setHoraFinalizacion(horaFinalizacion)
                .setHorasTrabajadas(horasDeTrabajoEnLaJornada);
        JornadaLaboral jornadaGuardada = jornadaLaboralRepositorio.save(jornada);

        // Hago esto porque la fecha aparece con la hora al momento de devolver la entidad creada. Ej: 2022-02-06T03:00:00.000+00:00
        // Con el DTO logro que la fecha se vea más limpia y ordenada. Ej: 06-02-2022
        return jornadaLaboralMapper.entidadADTOResponse(jornadaGuardada, dateFormater.format(jornadaGuardada.getFecha()));
    }

    @Override
    public JornadaLaboralResponse actualizarHorasDeUnaJornada(JornadaLaboralUpdateDTO dtoRequest) throws ParseException {
        // Convierto la fecha que recibo
        DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        dateFormater.setLenient(false);
        Date fecha = dateFormater.parse(dtoRequest.getFechaDeLaJornada());

        // Busco con el numero de documento y la fecha la jornada que se quiere cambiar
        JornadaLaboral jornadaLaboral = jornadaLaboralRepositorio.findByEmpleadoNumeroDeDocumentoAndFecha(dtoRequest.getNumeroDeDocumento(), fecha)
                .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado una jornada laboral con los datos provistos"));

        // Se formatea la nueva hora
        LocalTime horaFinalizacion = LocalTime.of(jornadaLaboral.getHoraComienzo().getHour() + dtoRequest.getNuevasHorasDeTrabajo(), 0);

        // Se calcula la nueva hora de salida
        int nuevasHorasTrabajadas = calcularHoras(jornadaLaboral.getHoraComienzo().getHour(), horaFinalizacion.getHour());

        // Corroboramos que las horas trabajdas no violen las restricciones de horas por turno
        // Sale una excepcion que la atrapo en el controlador
        corroborarTipoDeJornadaConSusHoras(jornadaLaboral.getTipoDeJornada().getNombre(), nuevasHorasTrabajadas);

        // Guardo la nueva hora de salida y las horas trabajadas
        jornadaLaboral.setHoraFinalizacion(horaFinalizacion);
        jornadaLaboral.setHorasTrabajadas(nuevasHorasTrabajadas);

        // Corroboro que las horas no excedan el limite semanal
        empleadoExcedeHorasSemanales(jornadaLaboral.getEmpleado().getId(), nuevasHorasTrabajadas, jornadaLaboral.getFecha());

        JornadaLaboral jornadaGuardada = jornadaLaboralRepositorio.save(jornadaLaboral);

        // Hago esto porque la fecha aparece con la hora al momento de devolver la entidad creada. Ej: 2022-02-06T03:00:00.000+00:00
        // Con el DTO logro que la fecha se vea más limpia y ordenada. Ej: 06-02-2022
        return jornadaLaboralMapper.entidadADTOResponse(jornadaGuardada, dateFormater.format(jornadaGuardada.getFecha()));
    }

    @Override
    public List<HorasPorTipoDeJornadaResponse> buscarPorCadaEmpleadoLasHorasCargadasPorTipoDeJornada() {
        // En los hashmap guardo una instancia del dto y la clave es el id del empleado
        // Si ya existe un tipo de jornada de un empleado guardado en un hashmap, le sumo las horas
        Map<Long, HorasPorTipoDeJornadaResponse> horasTipoJornadaNormalMap = new HashMap<>();
        Map<Long, HorasPorTipoDeJornadaResponse> horasTipoJornadaExtraMap = new HashMap<>();
        Map<Long, HorasPorTipoDeJornadaResponse> horasTipoJornadaCombinadaMap = new HashMap<>();

        List<JornadaLaboral> todasLasJornadas = jornadaLaboralRepositorio.findAll();

        // Por cada jornada veo que tipo de jornada es y lo guardo en hashmap correspondiente
        for(JornadaLaboral jornada : todasLasJornadas){
            switch (jornada.getTipoDeJornada().getNombre()){
                case ConstantesGlobales.TIPOS_DE_JORNADAS.TURNO_NORMAL:
                    // Corroboro si esta el empleado en el hashmap
                    if(horasTipoJornadaNormalMap.containsKey(jornada.getEmpleado().getId())){
                        // Agarro valor guardado de la clave (el id del empleado) y le sumo las nuevas horas
                        HorasPorTipoDeJornadaResponse aux = horasTipoJornadaNormalMap.get(jornada.getEmpleado().getId());
                        aux.setHorasTrabajadas(aux.getHorasTrabajadas() + jornada.getHorasTrabajadas());

                        // Reemplazo el valor existente por el nuevo en el hashpmap
                        horasTipoJornadaNormalMap.replace(jornada.getEmpleado().getId(), aux);
                    }else{
                        // Si no esta el empleado en el hashmap, creo una instancia de HorasPorTipoDeJornadaResponse y
                        // la guardo en el hashmap
                        horasTipoJornadaNormalMap.put(jornada.getEmpleado().getId(), new HorasPorTipoDeJornadaResponse()
                                .setNombreEmpleado(jornada.getEmpleado().getNombre())
                                .setApellidoEmpleado(jornada.getEmpleado().getApellido())
                                .setNumeroDeDocumento(jornada.getEmpleado().getNumeroDeDocumento())
                                .setTipoJornada(jornada.getTipoDeJornada().getNombre())
                                .setHorasTrabajadas(jornada.getHorasTrabajadas()));
                    }
                    break;

                case ConstantesGlobales.TIPOS_DE_JORNADAS.TURNO_EXTRA:
                    // Corroboro si esta el empleado en el hashmap
                    if(horasTipoJornadaExtraMap.containsKey(jornada.getEmpleado().getId())){
                        // Agarro valor guardado de la clave (el id del empleado) y le sumo las nuevas horas
                        HorasPorTipoDeJornadaResponse aux = horasTipoJornadaExtraMap.get(jornada.getEmpleado().getId());
                        aux.setHorasTrabajadas(aux.getHorasTrabajadas() + jornada.getHorasTrabajadas());

                        // Reemplazo el valor existente por el nuevo en el hashpmap
                        horasTipoJornadaExtraMap.replace(jornada.getEmpleado().getId(), aux);
                    }else{
                        // Si no esta el empleado en el hashmap, creo una instancia de HorasPorTipoDeJornadaResponse y
                        // la guardo en el hashmap
                        horasTipoJornadaExtraMap.put(jornada.getEmpleado().getId(), new HorasPorTipoDeJornadaResponse()
                                .setNombreEmpleado(jornada.getEmpleado().getNombre())
                                .setApellidoEmpleado(jornada.getEmpleado().getApellido())
                                .setNumeroDeDocumento(jornada.getEmpleado().getNumeroDeDocumento())
                                .setTipoJornada(jornada.getTipoDeJornada().getNombre())
                                .setHorasTrabajadas(jornada.getHorasTrabajadas()));
                    }
                    break;

                case ConstantesGlobales.TIPOS_DE_JORNADAS.TURNO_COMBINADO:
                    // Corroboro si esta el empleado en el hashmap
                    if(horasTipoJornadaCombinadaMap.containsKey(jornada.getEmpleado().getId())){
                        // Agarro valor guardado de la clave (el id del empleado) y le sumo las nuevas horas
                        HorasPorTipoDeJornadaResponse aux = horasTipoJornadaCombinadaMap.get(jornada.getEmpleado().getId());
                        aux.setHorasTrabajadas(aux.getHorasTrabajadas() + jornada.getHorasTrabajadas());

                        // Reemplazo el valor existente por el nuevo en el hashpmap
                        horasTipoJornadaCombinadaMap.replace(jornada.getEmpleado().getId(), aux);

                    }else{
                        // Si no esta el empleado en el hashmap, creo una instancia de HorasPorTipoDeJornadaResponse y
                        // la guardo en el hashmap
                        horasTipoJornadaCombinadaMap.put(jornada.getEmpleado().getId(), new HorasPorTipoDeJornadaResponse()
                                .setNombreEmpleado(jornada.getEmpleado().getNombre())
                                .setApellidoEmpleado(jornada.getEmpleado().getApellido())
                                .setNumeroDeDocumento(jornada.getEmpleado().getNumeroDeDocumento())
                                .setTipoJornada(jornada.getTipoDeJornada().getNombre())
                                .setHorasTrabajadas(jornada.getHorasTrabajadas()));
                    }
                    break;

                default:
                    // No se hace nada en este caso
            }
        }

        // Guardo el contenido de los hashmaps la clase Collection
        Collection<HorasPorTipoDeJornadaResponse> horasPorEmpleadoTipoNormal = horasTipoJornadaNormalMap.values();
        Collection<HorasPorTipoDeJornadaResponse> horasPorEmpleadoTipoExtras = horasTipoJornadaExtraMap.values();
        Collection<HorasPorTipoDeJornadaResponse> horasPorEmpleadoTipoCombinado = horasTipoJornadaCombinadaMap.values();

        List<HorasPorTipoDeJornadaResponse> listaConLasSumas = new ArrayList<>();

        // Guardo todos los valores en la lista
        listaConLasSumas.addAll(horasPorEmpleadoTipoNormal);
        listaConLasSumas.addAll(horasPorEmpleadoTipoExtras);
        listaConLasSumas.addAll(horasPorEmpleadoTipoCombinado);

        return listaConLasSumas;
    }

    private void empleadoExcedeHorasSemanales(Long idEmpleado, int horasDeLaJornada, Date fechaActual) throws HorasSemanalesExcedidasExcepcion {
        Date fechaSieteDiasAtras = restarSieteDiasAUnaFecha(fechaActual);

        // Busco todas las jornadas desde la fecha actual hasta 7 días atras
        // No importa si la lista esta vacia
        List<JornadaLaboral> jornadasEncontradas = jornadaLaboralRepositorio.buscarLasJornadasDeUnaSemanaDeUnEmpleado(idEmpleado,
                fechaActual, fechaSieteDiasAtras).get();

        // Obtengo todas las horas trabajadas en la semana de un empleado
        // Si la lista esta vacia, no se suma nada y devuelve 0
        int horasSemanales = jornadasEncontradas.stream()
                .mapToInt(JornadaLaboral::getHorasTrabajadas)
                .sum();

        // Si las horas semanales más las horas de la jornada exceden las 48 horas, lanzo excepción
        if (horasSemanales + horasDeLaJornada > 48) throw new HorasSemanalesExcedidasExcepcion("Horas semanales excedidas. Máximo 48 horas por semana");
    }

    private int calcularHoras(int horaInicio, int horaFinalizacion){
        int horasTrabajadas = horaFinalizacion - horaInicio;

         /*
            Suponiendo que un empleado ingresó a trabajar a las 21hs y termina de trabajar a las 5hs del día siguiente,
            da como resultado -16, al restar la hora de finalizacion (5)  y la hora de inicio (21)
            Si uso el valor absoluto de las horas trabajadas y se lo resto a 24, obtengo la cantidad de horas
            que se trabajo. En este caso serían 8 horas (24-16)
         */
        if(horasTrabajadas < 0){
            // Si entra acá es porque el calculo dio negativo. El empleado terminó de trabajar al día siguiente
            return 24 - Math.abs(horasTrabajadas);
        }

        return horasTrabajadas;
    }

    private void corroborarTipoDeJornadaConSusHoras(String nombreDeLaJornada, int horasTrabajadas) throws HorasDeLaJornadaExcepcion {
        if(nombreDeLaJornada.equalsIgnoreCase("Turno normal") && (horasTrabajadas < 6 || horasTrabajadas > 8)){
            throw new HorasDeLaJornadaExcepcion("Horario de trabajo invalido. En turno normal se permite trabajar de 6 a 8 horas.");

        }else if(nombreDeLaJornada.equalsIgnoreCase("Turno extra") && (horasTrabajadas < 2 || horasTrabajadas > 6)){
            throw new HorasDeLaJornadaExcepcion("Horario de trabajo invalido. En turno extra se permite trabajar de 2 a 6 horas.");

        }else if(nombreDeLaJornada.equalsIgnoreCase("Turno combinado") && (horasTrabajadas > 12)){
            throw new HorasDeLaJornadaExcepcion("Horario de trabajo invalido. En turnos combinados se permite trabajar un máximo de 12 horas.");
        }
    }

    private Date restarSieteDiasAUnaFecha(Date fecha){
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.add(Calendar.DATE, -7);

        // calendario.getTime devuelve un Date
        return calendario.getTime();
    }

    private boolean cantidadDeDiasLibresUsadosEnLaSemana(Long empleadoId, Long tipoJornadaId, Date fechaActual, Date fechaSieteDiasAtras){
        return jornadaLaboralRepositorio.cantidadDeDiasLibresEnLosUltimosSieteDias(
                empleadoId, tipoJornadaId, fechaActual, fechaSieteDiasAtras) < 2;
    }

    private void saberSiElEmpleadoEstaDeVacaciones(Empleado em) throws EmpleadoNoPuedeTrabajarExcepcion{
        if(em.isVacaciones()) throw new EmpleadoNoPuedeTrabajarExcepcion("Empleado de vacaciones");
    }

    private LocalTime asignarHoraFinalizacionCorrespondiente(String tipoDeJornada, Long idEmpleado, Date fechaDeLaJornada, String horaFinalizacion)
            throws EmpleadoDiaLibreExcepcion, DateTimeParseException {

        // Si se ingresa un día libre, corroboro si tiene días libres disponibles en la semana
        if(tipoDeJornada.equalsIgnoreCase(ConstantesGlobales.TIPOS_DE_JORNADAS.DIA_LIBRE)){
            // Si el empleado tiene menos de 2 días libres durante la semana, se le permite el día libre
            if(cantidadDeDiasLibresUsadosEnLaSemana(idEmpleado,  tipoDeJornadaLaboralServicio.obtenerElIdDelDiaLibre(),
                    fechaDeLaJornada,
                    restarSieteDiasAUnaFecha(fechaDeLaJornada))){

                // El día libre termina a las 23:59:59 del mismo dia
                return LocalTime.of(23, 59, 59);
            }else{
                throw new EmpleadoDiaLibreExcepcion("Empleado sin días libres disponibles durante la semana");
            }
        }

        // Si es un turno de trabajo, lo convierto a LocalTime
        // Esto tira una excepcion de tipo ParseException que se captura en el controlador
        return LocalTime.parse(horaFinalizacion);
    }

    private void saberSiElEmpleadoEstaEnDiaLibre(Long idEmpleado, Date fechaDeLaJornada) throws EmpleadoNoPuedeTrabajarExcepcion{
        boolean estaEnDiaLibre = jornadaLaboralRepositorio.existsByEmpleadoIdAndTipoDeJornadaIdAndFecha(idEmpleado,
                        tipoDeJornadaLaboralServicio.obtenerElIdDelDiaLibre(), fechaDeLaJornada);

        if(estaEnDiaLibre) throw new EmpleadoNoPuedeTrabajarExcepcion("Empleado en día libre");
    }

    private void existeMismoEmpleadoEnElMismoDiaYJornada(Long idEmpleado, Long idTipoJornada, Date fechaDeLaJornada) throws EmpleadoNoPuedeTrabajarExcepcion{
        if(jornadaLaboralRepositorio.existsByEmpleadoIdAndTipoDeJornadaIdAndFecha(idEmpleado, idTipoJornada, fechaDeLaJornada))
            throw new EmpleadoNoPuedeTrabajarExcepcion("Este empleado ya esta en esta jornada");
    }

    private void hayLugarEnElTurno(Long idJornada, Date fechaActual, String tipoJornada) throws TurnoCompletoExcepcion{
        if (jornadaLaboralRepositorio.countByTipoDeJornadaIdAndFecha(idJornada, fechaActual) == 2 &&
                !tipoJornada.equalsIgnoreCase(ConstantesGlobales.TIPOS_DE_JORNADAS.DIA_LIBRE))
            throw new TurnoCompletoExcepcion("No hay mas lugar en este turno por el día de hoy");
    }
}
