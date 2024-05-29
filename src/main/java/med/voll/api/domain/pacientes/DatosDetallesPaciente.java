package med.voll.api.domain.pacientes;

import med.voll.api.domain.direccion.DatosDireccion;

public record DatosDetallesPaciente(
        Long id,
        String nombre,
        String email,
        String telefono,
        String string,
        DatosDireccion direccion
) {
}
