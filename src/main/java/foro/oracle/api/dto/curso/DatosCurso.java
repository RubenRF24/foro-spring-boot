package foro.oracle.api.dto.curso;

import jakarta.validation.constraints.NotBlank;

public record DatosCurso(@NotBlank String nombre) {
}
