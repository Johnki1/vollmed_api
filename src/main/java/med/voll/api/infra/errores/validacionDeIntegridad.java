package med.voll.api.infra.errores;

public class validacionDeIntegridad extends RuntimeException {
    public validacionDeIntegridad(String s) {
        super(s);
    }
}
