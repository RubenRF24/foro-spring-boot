package foro.oracle.api.dto.usuario;

import foro.oracle.api.model.Usuario;

public record DatosListadoUsuarios(String login, String email) {

    public DatosListadoUsuarios(Usuario usuario){
        this(usuario.getLogin(), usuario.getEmail());
    }
}
