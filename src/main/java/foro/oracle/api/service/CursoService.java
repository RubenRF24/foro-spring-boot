package foro.oracle.api.service;

import foro.oracle.api.dto.curso.DatosActualizarCurso;
import foro.oracle.api.dto.curso.DatosListadoCursos;
import foro.oracle.api.model.Curso;
import foro.oracle.api.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> getAllCursos(){
        return cursoRepository.findAll();
    }

    public Page<DatosListadoCursos> getAllCursos(Pageable paginacion){
        return cursoRepository.findAll(paginacion).map(DatosListadoCursos::new);
    }

    public Curso findCurso(String nombre){
        return cursoRepository.findByNombre(nombre);
    }

    public Curso createCurso(Curso curso){
        return cursoRepository.save(curso);
    }

    public Curso updateCurso(String nombre, DatosActualizarCurso datosActualizarCurso){
        Curso curso = findCurso(nombre);
        curso.actualizarDatos(datosActualizarCurso);
        return cursoRepository.save(curso);
    }

    public void deleteCurso(String nombre){
        cursoRepository.delete(findCurso(nombre));
    }
}
