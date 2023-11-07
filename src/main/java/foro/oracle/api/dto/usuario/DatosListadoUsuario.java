package foro.oracle.api.dto.usuario;

import foro.oracle.api.model.Usuario;

public record DatosListadoUsuario(String login, String email) {

    public DatosListadoUsuario(Usuario usuario){
        this(usuario.getLogin(), usuario.getEmail());
    }
}
