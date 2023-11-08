package foro.oracle.api.controller;

import foro.oracle.api.dto.topico.DatosListadoTopicos;
import foro.oracle.api.infra.errores.TratadorDeErrores;
import foro.oracle.api.infra.security.TokenService;
import foro.oracle.api.model.Topico;
import foro.oracle.api.model.Usuario;
import foro.oracle.api.dto.usuario.*;
import foro.oracle.api.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DatosRespuestaUsuario> crearUsuario(@RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario,
                                                              UriComponentsBuilder uriComponentsBuilder){

        Usuario usuario = usuarioService.createUsuario(new Usuario(datosRegistroUsuario.login(), datosRegistroUsuario.email(), datosRegistroUsuario.clave()));
        DatosRespuestaUsuario datosRespuestaUsuario = new DatosRespuestaUsuario(usuario.getIdUsuario(), usuario.getLogin(), usuario.getEmail());

        URI url = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getIdUsuario()).toUri();

        return ResponseEntity.created(url).body(datosRespuestaUsuario);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoUsuarios>> listarUsuarios(@PageableDefault Pageable paginacion){
        return ResponseEntity.ok(usuarioService.getAllUsuario(paginacion));
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<DatosListadoUsuario> retornaDatosUsuario(@PathVariable String nombre){
        Usuario usuario = usuarioService.findUsuario(nombre);
        if(usuario == null){
            throw new EntityNotFoundException("Usuario no encontrado");
        }
        var topicos = usuario.getTopicoList();
        List<DatosListadoTopicos> topicoDTO = new ArrayList<>();
        for (Topico topico: topicos){
            DatosListadoTopicos dto = new DatosListadoTopicos(topico);
            topicoDTO.add(dto);
        }
        var datosTopico = new DatosListadoUsuario(usuario.getLogin(), usuario.getEmail(), topicoDTO);
        return ResponseEntity.ok(datosTopico);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaUsuario> actualizarUsuario(@PathVariable Long id, @RequestBody @Valid DatosActualizarUsuario datosActualizarUsuario,
                                                                   HttpServletRequest request){
        Usuario usuario = usuarioService.findUsuario(id);
        if (usuario == null){
            throw new EntityNotFoundException();
        }
        boolean esIgualUsuario = verificarUsuario(usuario, obtenerAutor(request));
        if(!esIgualUsuario){
            throw new TratadorDeErrores.CustomNonAuthoritativeInformationException("Usuario no autorizado");
        }
        usuario.actualizarDatos(datosActualizarUsuario);
        return ResponseEntity.ok(new DatosRespuestaUsuario(usuario.getIdUsuario(), usuario.getLogin(), usuario.getEmail()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id, HttpServletRequest request){
        Usuario usuario = usuarioService.findUsuario(id);
        if (usuario == null){
            throw new EntityNotFoundException();
        }
        boolean esIgualUsuario = verificarUsuario(usuario, obtenerAutor(request));
        if(!esIgualUsuario){
            throw new TratadorDeErrores.CustomNonAuthoritativeInformationException("Usuario no autorizado");
        }
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    public String obtenerAutor(HttpServletRequest request) {
        String nombreUsuario = null;
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            var token = authHeader.replace("Bearer ", "");
            nombreUsuario = tokenService.getSubject(token); // extract username
        }
        return nombreUsuario;
    }

    private boolean verificarUsuario(Usuario usuario, String nombreUsuario) {
        Usuario usuarioLogeado = usuarioService.findUsuario(nombreUsuario);
        return (usuarioLogeado.getLogin() == usuario.getLogin());
    }

}
