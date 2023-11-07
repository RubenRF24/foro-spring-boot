package foro.oracle.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import foro.oracle.api.dto.topico.DatosActualizarTopico;
import foro.oracle.api.dto.topico.DatosRegistroTopico;
import foro.oracle.api.dto.topico.StatusTopico;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idTopico")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_topico")
    private Long idTopico;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus")
    private StatusTopico estatus = StatusTopico.NO_RESPONDIDO;

    @ManyToOne
    @JoinColumn(name = "autor")
    private Usuario autor;

    @ManyToOne
    @JoinColumn(name = "curso")
    private Curso curso;

    @Column(name = "cerrado")
    private Boolean cerrado = false;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Respuesta> respuestaList = new ArrayList<>();

    public Topico(DatosRegistroTopico datosRegistroMedico, Usuario autor, Curso curso) {
        this.titulo = datosRegistroMedico.titulo();
        this.mensaje = datosRegistroMedico.mensaje();
        this.autor = autor;
        this.curso = curso;
    }

    public void actualizarDatos(DatosActualizarTopico datosActualizarTopico, Curso curso) {
        if(datosActualizarTopico.titulo() != null){
            this.titulo = datosActualizarTopico.titulo();
        }
        if(datosActualizarTopico.mensaje() != null){
            this.mensaje = datosActualizarTopico.mensaje();
        }
        if(datosActualizarTopico.curso() != null){
            this.curso = curso;
        }
    }
}
