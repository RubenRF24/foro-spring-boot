package foro.oracle.api.repository;

import foro.oracle.api.model.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespuestasRepository extends JpaRepository<Respuesta, Long> {
    Respuesta findByIdRespuesta(Long idRespuesta);
}
