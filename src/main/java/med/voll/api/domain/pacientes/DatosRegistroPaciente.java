package med.voll.api.domain.pacientes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.Especialidad;

public record DatosRegistroPaciente(
        @NotBlank
        String nombre,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Pattern(regexp = "\\d{7,10}")
        String telefono,
        @NotBlank
        @Pattern(regexp = "\\d{4,10}")
        String documento,
        @NotNull
        @Valid
        DatosDireccion direccion
) {
}
