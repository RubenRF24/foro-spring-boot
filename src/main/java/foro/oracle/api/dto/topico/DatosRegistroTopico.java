package foro.oracle.api.dto.topico;

import foro.oracle.api.dto.curso.DatosCurso;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroTopico(
        @NotBlank String titulo,
        @NotBlank String mensaje,
        @NotNull @Valid DatosCurso curso) {
}
