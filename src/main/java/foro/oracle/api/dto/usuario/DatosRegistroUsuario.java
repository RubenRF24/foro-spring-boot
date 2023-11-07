package foro.oracle.api.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroUsuario(
        @NotBlank String login,
        @NotBlank String email,
        @NotBlank String clave) {
}
