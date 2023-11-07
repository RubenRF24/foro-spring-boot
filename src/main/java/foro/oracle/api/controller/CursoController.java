package foro.oracle.api.controller;

import foro.oracle.api.dto.curso.*;
import foro.oracle.api.dto.topico.DatosListadoTopicos;
import foro.oracle.api.model.Curso;
import foro.oracle.api.model.Topico;
import foro.oracle.api.service.CursoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public ResponseEntity<DatosRespuestaCurso> registrarCurso(@RequestBody @Valid DatosRegistroCurso datosRegistroCurso,
                               UriComponentsBuilder uriComponentsBuilder){
        Curso curso = cursoService.createCurso(new Curso(datosRegistroCurso));
        DatosRespuestaCurso datosRespuestaCurso = new DatosRespuestaCurso(curso.getId(), curso.getNombre(), curso.getCategoria().toString());

        URI url = uriComponentsBuilder.path("/cursos/{nombre}").buildAndExpand(curso.getNombre()).toUri();

        return ResponseEntity.created(url).body(datosRespuestaCurso);

    }

    @PutMapping("/{nombre}")
    @Transactional
    public ResponseEntity<DatosRespuestaCurso> actualizarCurso(@PathVariable String nombre, @RequestBody DatosActualizarCurso datosActualizarCurso){
        Curso curso = cursoService.updateCurso(nombre, datosActualizarCurso);
        return ResponseEntity.ok(new DatosRespuestaCurso(curso.getId(), curso.getNombre(), curso.getCategoria().toString()));
    }

    @DeleteMapping("/{nombre}")
    @Transactional
    public ResponseEntity<?> eliminarCurso(@PathVariable String nombre){
        cursoService.deleteCurso(nombre);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<Page<DatosListadoCursos>> listadoCursos(@PageableDefault Pageable paginacion){
        return ResponseEntity.ok(cursoService.getAllCursos(paginacion));
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<DatosListadoCurso> obtenerCurso(@PathVariable String nombre){
        Curso curso = cursoService.findCurso(nombre);
        if(curso == null){
            throw new EntityNotFoundException();
        }
        var topicos = curso.getTopicoList();
        List<DatosListadoTopicos> topicosDTO = new ArrayList<>();
        for (Topico topico: topicos){
            DatosListadoTopicos dto = new DatosListadoTopicos(topico);
            topicosDTO.add(dto);
        }
        var datosCurso = new DatosListadoCurso(curso.getNombre(), curso.getCategoria().toString(),
                topicosDTO);

        return ResponseEntity.ok(datosCurso);
    }


}
