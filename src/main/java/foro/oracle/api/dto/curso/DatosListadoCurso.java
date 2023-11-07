package foro.oracle.api.dto.curso;

import foro.oracle.api.dto.topico.DatosListadoTopicos;

import java.util.List;

public record DatosListadoCurso(String nombre, String categoria, List<DatosListadoTopicos>topicoList) {
}
