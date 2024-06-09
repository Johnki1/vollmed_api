package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {
    //
    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico,
                                                                UriComponentsBuilder uriComponentsBuilder){
        Medico medico = medicoRepository.save(new Medico(datosRegistroMedico));
        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico.getId(),medico.getNombre(),medico.getEmail(),
                medico.getTelefono(),medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(),medico.getDireccion().getNumero(),medico.getDireccion().getComplemento()));
        //Return 201 Created
        //URL donde encontrar al medico
        //GET http://localhost:8080/medico/id-medicoCredo
        URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaMedico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosLsitadoMedico>> ListadoMedico(@PageableDefault(size = 20,sort = "nombre") Pageable paginacion){
//        return medicoRepository.findAll(paginacion).map(DatosLsitadoMedico::new);
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion)
                .map(DatosLsitadoMedico::new)) ;
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActulizarMedico datosActulizarMedico){
        Medico medico = medicoRepository.getReferenceById(datosActulizarMedico.id());
        medico.actualizarDatos(datosActulizarMedico);
        return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(),medico.getNombre(),medico.getEmail(),
                medico.getTelefono(),medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),
                    medico.getDireccion().getCiudad(),medico.getDireccion().getNumero(),medico.getDireccion().getComplemento())));
    }

    /*
    ELIMINAR TOTALMENTE DE UNA MANERA DINAMICA
    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medicoRepository.delete(medico);
    }
    */

    //DELETE Logico excluir los que no esten activos, se usa bandera
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaMedico> retornoDatosMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        var datosMedicos = new DatosRespuestaMedico(medico.getId(),medico.getNombre(),medico.getEmail(),
                medico.getTelefono(),medico.getEspecialidad().toString(),
                new DatosDireccion(medico.getDireccion().getCalle(),medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(),medico.getDireccion().getNumero(),medico.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosMedicos);
    }


}
