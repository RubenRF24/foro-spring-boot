package foro.oracle.api.dto.respuesta;

import foro.oracle.api.model.Respuesta;
import foro.oracle.api.model.Topico;

import java.time.LocalDateTime;

public record DatosListadoRespuesta(Long idRespuesta,String mensaje, String autor, LocalDateTime fechaCreacion, Boolean solucion) {
    public DatosListadoRespuesta(Respuesta respuesta) {
        this(respuesta.getIdRespuesta(), respuesta.getMensaje(), respuesta.getAutor().getLogin(), respuesta.getFechaCreacion(), respuesta.getSolucion());
    }
}
