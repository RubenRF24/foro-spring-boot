package foro.oracle.api.dto.usuario;

import foro.oracle.api.dto.topico.DatosListadoTopicos;
import java.util.List;

public record DatosListadoUsuario(String login, String email, List<DatosListadoTopicos> topicoList) {
}
