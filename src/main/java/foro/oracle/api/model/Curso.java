package foro.oracle.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import foro.oracle.api.dto.curso.Categoria;
import foro.oracle.api.dto.curso.DatosActualizarCurso;
import foro.oracle.api.dto.curso.DatosRegistroCurso;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "cursos")
@Entity(name = "Curso")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private Categoria categoria;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Topico> topicoList = new ArrayList<>();

    public Curso(DatosRegistroCurso datosRegistroCurso) {
        this.nombre = datosRegistroCurso.nombre();
        this.categoria = datosRegistroCurso.categoria();
    }

    public void actualizarDatos(DatosActualizarCurso datosActualizarCurso) {
        if(datosActualizarCurso.nombre() != null){
            this.nombre = datosActualizarCurso.nombre();
        }
        if(datosActualizarCurso.categoria() != null){
            this.categoria = datosActualizarCurso.categoria();
        }
    }
}
