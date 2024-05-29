package med.voll.api.domain.medico;

public record DatosLsitadoMedico(
        Long id,
        String nombre,
        String especialidad,
        String documento,
        String email
) {
    public DatosLsitadoMedico(Medico medico){
        this(medico.getId(), medico.getNombre(),medico.getEspecialidad().toString(),medico.getDocumento(), medico.getEmail());
    }
}
