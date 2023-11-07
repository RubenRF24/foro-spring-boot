package foro.oracle.api.dto.usuario;

import jakarta.validation.constraints.Email;

public record DatosActualizarUsuario(String login, @Email String email, String clave) {
}
