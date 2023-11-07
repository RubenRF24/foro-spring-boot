package foro.oracle.api.controller;

import foro.oracle.api.model.Usuario;
import foro.oracle.api.dto.usuario.*;
import foro.oracle.api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaUsuario> crearUsuario(@RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario,
                                                              UriComponentsBuilder uriComponentsBuilder){

        Usuario usuario = usuarioRepository.save(new Usuario(datosRegistroUsuario.login(), datosRegistroUsuario.email(), datosRegistroUsuario.clave()));
        DatosRespuestaUsuario datosRespuestaUsuario = new DatosRespuestaUsuario(usuario.getIdUsuario(), usuario.getLogin(), usuario.getEmail());

        URI url = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getIdUsuario()).toUri();

        return ResponseEntity.created(url).body(datosRespuestaUsuario);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoUsuario>> listarUsuarios(@PageableDefault Pageable paginacion){
        return ResponseEntity.ok(usuarioRepository.findAll(paginacion).map(DatosListadoUsuario::new));
    }

    @GetMapping("{id}")

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaUsuario> actualizarUsuario(@PathVariable Long id, @RequestBody @Valid DatosActualizarUsuario datosActualizarUsuario){
        Usuario usuario = usuarioRepository.findByIdUsuario(id);
        if (usuario == null){
            throw new EntityNotFoundException();
        }
        usuario.actualizarDatos(datosActualizarUsuario);
        return ResponseEntity.ok(new DatosRespuestaUsuario(usuario.getIdUsuario(), usuario.getLogin(), usuario.getEmail()));
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id){
        Usuario usuario = usuarioRepository.findByIdUsuario(id);
        usuarioRepository.delete(usuario);
        return ResponseEntity.noContent().build();
    }

}
