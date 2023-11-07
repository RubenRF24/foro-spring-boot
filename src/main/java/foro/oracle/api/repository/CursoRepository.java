package foro.oracle.api.repository;

import foro.oracle.api.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    Curso findByNombre(String curso);
}
