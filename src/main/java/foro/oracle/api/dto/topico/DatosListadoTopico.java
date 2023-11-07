package foro.oracle.api.dto.topico;

import foro.oracle.api.dto.respuesta.DatosListadoRespuesta;
import foro.oracle.api.model.Topico;

import java.time.LocalDateTime;
import java.util.List;

public record DatosListadoTopico(String titulo, String mensaje, LocalDateTime fechaCreacion, String estatus, String autor, String curso, List<DatosListadoRespuesta> respuestaList) {


}
