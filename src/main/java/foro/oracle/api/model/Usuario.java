package foro.oracle.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import foro.oracle.api.dto.usuario.DatosActualizarUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idUsuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "login")
    private String login;

    @Column(name = "email")
    private String email;

    @Column(name = "clave")
    private String clave;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Topico> topicoList = new ArrayList<>();

    public Usuario(String login, String email, String clave) {
        this.login = login;
        this.email = email;
        this.clave = encriptarClave(clave);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return clave;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void actualizarDatos(DatosActualizarUsuario datosActualizarUsuario) {
        if(datosActualizarUsuario.login() != null){
            this.login = datosActualizarUsuario.login();
        }
        if(datosActualizarUsuario.email() != null){
            this.email = datosActualizarUsuario.email();
        }
        if(datosActualizarUsuario.clave() != null){
            this.clave = encriptarClave(datosActualizarUsuario.clave());
        }
    }

    public String encriptarClave(String clave){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(clave);
    }
}
