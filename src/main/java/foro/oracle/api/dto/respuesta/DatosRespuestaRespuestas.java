package foro.oracle.api.dto.respuesta;

import java.time.LocalDateTime;

public record DatosRespuestaRespuestas(Long idRespuesta, String mensaje, String topico, LocalDateTime fechaCreacion, String autor) {
}
