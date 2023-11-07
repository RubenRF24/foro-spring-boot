package foro.oracle.api.dto.topico;

import java.time.LocalDateTime;

public record DatosRespuestaTopico(Long idTopico, String titulo, String mensaje, LocalDateTime fechaCreacion, String estatus, String autor, String curso) {
}
