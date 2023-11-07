package foro.oracle.api.controller;

import foro.oracle.api.dto.respuesta.DatosRegistroRespuesta;
import foro.oracle.api.dto.respuesta.DatosRespuestaRespuestas;
import foro.oracle.api.dto.topico.StatusTopico;
import foro.oracle.api.infra.errores.TratadorDeErrores;
import foro.oracle.api.infra.security.TokenService;
import foro.oracle.api.model.Respuesta;
import foro.oracle.api.model.Topico;
import foro.oracle.api.model.Usuario;
import foro.oracle.api.repository.RespuestasRepository;
import foro.oracle.api.service.CursoService;
import foro.oracle.api.service.RespuestaService;
import foro.oracle.api.service.TopicoService;
import foro.oracle.api.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/respuestas")
public class RespuestaController {

    @Autowired
    private TopicoService topicoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RespuestaService respuestaService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/{id}")
    public ResponseEntity<DatosRespuestaRespuestas> RegistrarRespuesta(@RequestBody @Valid DatosRegistroRespuesta datosRegistroRespuesta,
                                                                       @PathVariable Long id,
                                                                       HttpServletRequest request,
                                                                       UriComponentsBuilder uriComponentsBuilder){
        Topico topico = topicoService.findTopico(id);
        if (topico.getCerrado()){
            throw new TratadorDeErrores.CustomNonAuthoritativeInformationException("Topico cerrado");
        }
        verificarTopico(topico);
        Usuario usuario = usuarioService.findUsuario(obtenerAutor(request));

        Respuesta respuesta = respuestaService.createRespuesta(new Respuesta(datosRegistroRespuesta, topico, usuario));
        topicoService.updateEstatus(topico);
        DatosRespuestaRespuestas datosRespuestaRespuestas = new DatosRespuestaRespuestas(respuesta.getIdRespuesta(), respuesta.getMensaje(), respuesta.getTopico().getTitulo(),
                respuesta.getFechaCreacion(), respuesta.getAutor().getLogin());
        URI url = uriComponentsBuilder.path("/respuestas/{idTopico}/{idRespuesta}").buildAndExpand(topico.getIdTopico(), respuesta.getIdRespuesta()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaRespuestas);
    }

    @PutMapping("/{idTopico}/{idRespuesta}/marcar-solucion")
    @Transactional
    public ResponseEntity<?> marcarRespuestaComoSolucion(@PathVariable Long idTopico, @PathVariable Long idRespuesta,
                                                      HttpServletRequest request){
        Topico topico = topicoService.findTopico(idTopico);
        Respuesta respuesta = respuestaService.findRespuesta(idRespuesta, topico);
        verificarRespuesta(respuesta);

        Usuario usuario = usuarioService.findUsuario(obtenerAutor(request));
        verificarAutorTopico(usuario, topico);

        respuestaService.desmarcarOtrasRespuestasComoSolucion(topico, idRespuesta);
        boolean statusIguales = topico.getEstatus().toString() == StatusTopico.SOLUCIONADO.toString();
        if(!statusIguales){
            topicoService.updateEstatus(topico, StatusTopico.SOLUCIONADO);
        }

        respuesta.setSolucion(true);
        respuestaService.updateRespuesta(respuesta);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarRespuesta(@PathVariable Long id, HttpServletRequest request){
        Respuesta respuesta = respuestaService.findRespuesta(id);
        verificarRespuesta(respuesta);
        Topico topico = topicoService.findTopico(respuesta.getTopico().getIdTopico());
        verificarTopico(topico);

        Usuario usuario = usuarioService.findUsuario(obtenerAutor(request));
        boolean usuarioAutorizado = (verificarAutorRespuesta(usuario, respuesta) || verificarAutorTopico(usuario, topico));
        if(!usuarioAutorizado){
            usuarioNoAutorizado();
        }

        boolean statusIguales = topico.getEstatus().toString() == StatusTopico.SOLUCIONADO.toString();
        if(statusIguales){
            topicoService.updateEstatus(topico, StatusTopico.NO_SOLUCIONADO);
        }

        respuestaService.deleteRespuesta(id);


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

    private static void usuarioNoAutorizado() {
        throw new TratadorDeErrores.CustomNonAuthoritativeInformationException("Autor no valido");
    }

    private static boolean verificarAutorTopico(Usuario usuario, Topico topico) {
        return usuario.getIdUsuario() == topico.getAutor().getIdUsuario();
    }

    private static boolean verificarAutorRespuesta(Usuario usuario, Respuesta respuesta) {
        return usuario.getIdUsuario() == respuesta.getAutor().getIdUsuario();
    }

    private static void verificarRespuesta(Respuesta respuesta) {
        if (respuesta == null){
            throw new EntityNotFoundException("Respuesta no encontrada");
        }
    }

    private static void verificarTopico(Topico topico) {
        if (topico == null){
            throw new EntityNotFoundException("Topico no encontrado");
        }
    }
}
