package foro.oracle.api.repository;

import foro.oracle.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByLogin(String nombreUsuario);

    Usuario findByIdUsuario(Long id);
}
