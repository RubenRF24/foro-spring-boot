package foro.oracle.api.repository;


import foro.oracle.api.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    Topico findByIdTopico(Long id);
}
