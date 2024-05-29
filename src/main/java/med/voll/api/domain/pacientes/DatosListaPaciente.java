package med.voll.api.domain.pacientes;

import med.voll.api.domain.medico.Medico;

public record DatosListaPaciente(
        Long id,
        String nombre,
        String documento,
        String email
) {
    public DatosListaPaciente(Paciente paciente){
        this(paciente.getId(), paciente.getNombre(),paciente.getDocumento(), paciente.getEmail());
    }
}
