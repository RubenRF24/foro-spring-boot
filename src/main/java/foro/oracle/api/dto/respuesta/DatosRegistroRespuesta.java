package foro.oracle.api.dto.respuesta;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroRespuesta(@NotBlank String mensaje) {
}
