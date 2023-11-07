package foro.oracle.api.dto.topico;

import foro.oracle.api.dto.curso.DatosCurso;

public record DatosActualizarTopico(String titulo, String mensaje, DatosCurso curso) {
}
