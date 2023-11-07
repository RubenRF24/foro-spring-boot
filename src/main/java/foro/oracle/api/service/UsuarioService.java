package foro.oracle.api.service;

import foro.oracle.api.model.Usuario;
import foro.oracle.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAllUsuario(){
        return usuarioRepository.findAll();
    }

    public Usuario findUsuario(String nombre){
        return (Usuario) usuarioRepository.findByLogin(nombre);
    }

    public Usuario createUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Long id){
        usuarioRepository.deleteById(id);
    }
}
