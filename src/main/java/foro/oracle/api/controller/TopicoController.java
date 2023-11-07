package foro.oracle.api.controller;

import foro.oracle.api.dto.respuesta.DatosListadoRespuesta;
import foro.oracle.api.dto.respuesta.DatosRegistroRespuesta;
import foro.oracle.api.dto.respuesta.DatosRespuestaRespuestas;
import foro.oracle.api.infra.errores.TratadorDeErrores;
import foro.oracle.api.model.Curso;
import foro.oracle.api.model.Respuesta;
import foro.oracle.api.model.Topico;
import foro.oracle.api.dto.topico.*;
import foro.oracle.api.model.Usuario;
import foro.oracle.api.infra.security.TokenService;
import foro.oracle.api.service.CursoService;
import foro.oracle.api.service.RespuestaService;
import foro.oracle.api.service.TopicoService;
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
@RequestMapping("/topicos")
public class TopicoController {

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

    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> RegistrarTopicos(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                                                 UriComponentsBuilder uriComponentsBuilder,
                                                                 HttpServletRequest request){

        Curso curso = cursoService.findCurso(datosRegistroTopico.curso().nombre());
        Usuario usuario = usuarioService.findUsuario(obtenerAutor(request));
        Topico topico = topicoService.createTopico(new Topico(datosRegistroTopico, usuario, curso));
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico.getIdTopico(),topico.getTitulo(),
                topico.getMensaje(), topico.getFechaCreacion(), topico.getEstatus().toString(),
                topico.getAutor().getLogin(), topico.getCurso().getNombre());
        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getIdTopico()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopicos>> listadoTopicos(@PageableDefault Pageable paginacion){
        return ResponseEntity.ok(topicoService.getAllTopico(paginacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosListadoTopico> retornaDatosTopico(@PathVariable Long id){
        Topico topico = topicoService.findTopico(id);
        if(topico == null){
            throw new EntityNotFoundException();
        }
        var respuestas = topico.getRespuestaList();
        List<DatosListadoRespuesta> respuestasDTO = new ArrayList<>();
        for (Respuesta respuesta: respuestas){
            DatosListadoRespuesta dto = new DatosListadoRespuesta(respuesta);
            respuestasDTO.add(dto);
        }
        var datosTopico = new DatosListadoTopico(topico.getTitulo(), topico.getMensaje(),
                topico.getFechaCreacion(), topico.getEstatus().toString(), topico.getAutor().getLogin(),
                topico.getCurso().getNombre(), respuestasDTO);
        return ResponseEntity.ok(datosTopico);

    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(@PathVariable Long id, @RequestBody DatosActualizarTopico datosActualizarTopico){
        Topico topico = topicoService.getTopico(id);
        verificarTopico(topico);
        Curso curso = cursoService.findCurso(datosActualizarTopico.curso().nombre());

        topico.actualizarDatos(datosActualizarTopico, curso);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico.getIdTopico(), topico.getTitulo(), topico.getMensaje(),
                topico.getFechaCreacion(), topico.getEstatus().toString(), topico.getAutor().getLogin(), topico.getCurso().getNombre()));

    }



    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id, HttpServletRequest request){
        Topico topico = topicoService.findTopico(id);
        Usuario usuario = usuarioService.findUsuario(obtenerAutor(request));
        verificarAutorTopico(usuario, topico);
        topicoService.deleteTopico(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idTopico}/cerrar-topico")
    @Transactional
    public ResponseEntity<?> cerrarTopico(@PathVariable Long idTopico,
                                       HttpServletRequest request){
        Topico topico = topicoService.findTopico(idTopico);
        verificarTopico(topico);

        Usuario usuario = usuarioService.findUsuario(obtenerAutor(request));
        verificarAutorTopico(usuario, topico);

        boolean topicoCerrado = topico.getCerrado() == true;
        if(!topicoCerrado){
            topico.setCerrado(true);
        }

        topicoService.updateEstatus(topico, StatusTopico.CERRADO);

        return ResponseEntity.ok().build();
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

    private static void verificarAutorTopico(Usuario usuario, Topico topico) {
        boolean iguales = usuario.getIdUsuario() == topico.getAutor().getIdUsuario();
        if (!iguales){
            throw new TratadorDeErrores.CustomNonAuthoritativeInformationException("Autor no valido");
        }
    }

    private static void verificarTopico(Topico topico) {
        if (topico == null){
            throw new EntityNotFoundException("Topico no encontrado");
        }
    }


}
