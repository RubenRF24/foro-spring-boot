package foro.oracle.api.model;

import foro.oracle.api.dto.respuesta.DatosRegistroRespuesta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "respuestas")
@Entity(name = "Respuesta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idRespuesta")
public class Respuesta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_respuesta")
	private Long idRespuesta;

	@Column(name = "mensaje")
	private String mensaje;

	@ManyToOne
	@JoinColumn(name = "topico")
	private Topico topico;

	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion = LocalDateTime.now();

	@ManyToOne
	@JoinColumn(name = "autor")
	private Usuario autor;

	@Column(name = "solucion")
	private Boolean solucion = false;

	public Respuesta(DatosRegistroRespuesta datosRegistroRespuesta, Topico topico, Usuario autor) {
		this.mensaje = datosRegistroRespuesta.mensaje();
		this.topico = topico;
		this.autor = autor;
	}

}
