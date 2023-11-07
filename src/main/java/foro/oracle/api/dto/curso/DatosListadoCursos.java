package foro.oracle.api.dto.curso;

import foro.oracle.api.model.Curso;

public record DatosListadoCursos(String nombre, String categoria) {

    public DatosListadoCursos(Curso curso){
        this(curso.getNombre(), curso.getCategoria().toString());
    }
}
