package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.cancelarCitas.ValidadorCancelamientoDeConsulta;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.pacientes.Paciente;
import med.voll.api.domain.pacientes.PacienteRepository;
import med.voll.api.infra.errores.validacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultaService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    List<ValidadorDeConsultas> validadores;
    List<ValidadorCancelamientoDeConsulta> validadorCancelamiento;

    public DatosDetalleConsulta agendar (DatosAgendarConsulta datos){

        if (!pacienteRepository.findById(datos.idPaciente()).isPresent()){
            throw new validacionDeIntegridad("este id para el paciente no fue encontrado");
        }

        if (datos.idMedico() !=null  && !medicoRepository.existsById(datos.idMedico())){
            throw new validacionDeIntegridad("este id para el medico no fue encontrado");
        }

        validadores.forEach(v-> v.validar(datos));

        var paciente = pacienteRepository.findById(datos.idPaciente()).get();
        var medico = seleccionarMedico(datos);

        if (medico== null){
            throw new validacionDeIntegridad("No existen medicos disponibles para este horario y especialidad");
        }

        var consulta = new Consulta(medico, paciente, datos.fecha());
        consultaRepository.save(consulta);
        return new DatosDetalleConsulta(consulta);
    }

    public void cancelar (DatosCancelamientoConsulta datosCancelarConsulta){
        if (!consultaRepository.existsById(datosCancelarConsulta.idConsulta())){
            throw new validacionDeIntegridad("Id de la consulta informado no existe");
        }
        validadorCancelamiento.forEach(v->v.validar(datosCancelarConsulta));
        var consulta = consultaRepository.getReferenceById(datosCancelarConsulta.idConsulta());
        consulta.cancelar(datosCancelarConsulta.motivo());
    }

    private Medico seleccionarMedico(DatosAgendarConsulta datos) {

        if (datos.idMedico() != null){
            return medicoRepository.getReferenceById(datos.idMedico());
        }
        if (datos.especialidad() == null){
            throw new validacionDeIntegridad("Debe seleccionar una especialidad para el medico");
        }
        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datos.especialidad(),datos.fecha());
    }

}
