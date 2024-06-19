package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.pacientes.DatosRegistroPaciente;
import med.voll.api.domain.pacientes.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("deberia retornar nulo cuando el medico este en consulta en ese horario")
    void seleccionarMedicoConEspecialidadEnFechaCaso1() {
        //dado un caso de valores
        var proximoLunes10H= LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);

        var medico=registrarMedico("Jose","jose@gmail.com","123456",Especialidad.CARDIOLOGIA);
        var paciente=registrarPaciente("Antonio","antonio@gmail.com","641534");
        registrarConsulta(medico,paciente, proximoLunes10H);

        //cuando
        var medicoLibre = medicoRepository.seleccionarMedicoConEspecialidadEnFecha(Especialidad.CARDIOLOGIA,proximoLunes10H);
        //entonces
        assertThat(medicoLibre).isNull();
        //assertNull();

    }

//    @Test
//    @DisplayName("deberia retornar UN MEDICO cuando se realice la consulta en la base de datos para ese horario")
//    void seleccionarMedicoConEspecialidadEnFechaCaso2() {
//
//        var proximoLunes10H= LocalDate.now()
//                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
//                .atTime(10,0);
//
//        var medico=registrarMedico("Jose","jose@gmail.com","123456",Especialidad.CARDIOLOGIA);
//
//        var medicoLibre = medicoRepository.seleccionarMedicoConEspecialidadEnFecha(Especialidad.CARDIOLOGIA,proximoLunes10H);
//
//        assertThat(medicoLibre).isEqualTo(medico);
//
//    }

    private void registrarConsulta (Medico medico, Paciente paciente, LocalDateTime fecha){
        em.persist(new Consulta(null,medico,paciente, fecha,null));
    }
    private Medico registrarMedico(String nombre, String email, String documento,Especialidad especialidad){
        var medico = new Medico(datosMedico(nombre,email,documento,especialidad));
        em.persist(medico);
        return medico;
    }

    private Paciente registrarPaciente(String nombre, String email, String documento){
        var paciente = new Paciente(datosPaciente(nombre, email, documento));
        em.persist(paciente);
        return paciente;
    }

    private DatosRegistroMedico datosMedico (String nombre, String email,String documento, Especialidad especialidad){
        return new DatosRegistroMedico(
                nombre,
                email,
                "619999999",
                documento,
                especialidad,
                datosDireccion()
        );
    }

    private DatosRegistroPaciente datosPaciente(String nombre, String email, String documento){
        return new DatosRegistroPaciente(
                nombre,
                email,
                "619999999",
                documento,
                datosDireccion()
        );
    }

    private DatosDireccion datosDireccion(){
        return new DatosDireccion(
                "36A",
                "Antioquia",
                "Guarne",
                "55-44",
                "1"

        );
    }
}