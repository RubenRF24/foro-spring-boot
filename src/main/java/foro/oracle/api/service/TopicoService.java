package foro.oracle.api.service;

import foro.oracle.api.dto.topico.DatosListadoTopicos;
import foro.oracle.api.dto.topico.StatusTopico;
import foro.oracle.api.model.Respuesta;
import foro.oracle.api.model.Topico;
import foro.oracle.api.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    public List<Topico> getAllTopico(){
        return topicoRepository.findAll();
    }

    public Page<DatosListadoTopicos> getAllTopico(Pageable paginacion){
        return topicoRepository.findAll(paginacion).map(DatosListadoTopicos::new);
    }

    public Topico getTopico(Long id){
        return topicoRepository.getReferenceById(id);
    }

    public Topico findTopico(Long id){
        return topicoRepository.findByIdTopico(id);
    }

    public Topico createTopico(Topico topico){
        return topicoRepository.save(topico);
    }

    public void updateTopico(Topico topico){
        topicoRepository.save(topico);
    }

    public void deleteTopico(Long id){
        topicoRepository.deleteById(id);
    }

    public void updateEstatus(Topico topico) {
        List<Respuesta> respuestas = topico.getRespuestaList();
        var status = StatusTopico.NO_SOLUCIONADO;
        if(respuestas.isEmpty()){
            status = StatusTopico.NO_RESPONDIDO;
        }else{
            for (Respuesta respuesta: respuestas) {
                if(respuesta != null){
                    if (respuesta.getSolucion()) {
                        status = StatusTopico.SOLUCIONADO;
                    }
                }
            }
        }
        if(status != topico.getEstatus()){
            topico.setEstatus(status);
            updateTopico(topico);
        }
    }

    public void updateEstatus(Topico topico, StatusTopico status) {
        topico.setEstatus(status);
        updateTopico(topico);

    }
}
