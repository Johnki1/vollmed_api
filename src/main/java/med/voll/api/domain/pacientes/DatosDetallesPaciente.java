package med.voll.api.domain.pacientes;

import med.voll.api.domain.direccion.Direccion;

public record DatosDetallesPaciente(
        Long id,
        String nombre,
        String email,
        String telefono,
        String string,
        Direccion direccion
) {
    public DatosDetallesPaciente(Paciente paciente) {
        this(paciente.getId(), paciente.getNombre(), paciente.getEmail(), paciente.getDocumento(), paciente.getTelefono(), paciente.getDireccion());
    }
}
