package foro.oracle.api.dto.topico;

import foro.oracle.api.model.Topico;

import java.time.LocalDateTime;

public record DatosListadoTopicos(String titulo, String mensaje, LocalDateTime fechaCreacion, String estatus, String autor, String curso) {

    public DatosListadoTopicos(Topico topico){
        this(topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion(), topico.getEstatus().toString(), topico.getAutor().getLogin(), topico.getCurso().getNombre());
    }

}
