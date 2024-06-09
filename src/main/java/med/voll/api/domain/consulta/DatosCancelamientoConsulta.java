package med.voll.api.domain.consulta;

import java.time.LocalDateTime;

public record DatosCancelamientoConsulta(
        Long id,
        Long idConsulta,
        MotivoCancelamiento motivo

) {

}
