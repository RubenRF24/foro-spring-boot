package foro.oracle.api.service;

import foro.oracle.api.model.Respuesta;
import foro.oracle.api.model.Topico;
import foro.oracle.api.repository.RespuestasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RespuestaService {

    @Autowired
    private RespuestasRepository respuestasRepository;

    public Respuesta createRespuesta(Respuesta respuesta){
        return respuestasRepository.save(respuesta);
    }

    public Respuesta findRespuesta(Long idRespuesta, Topico topico) {
        Respuesta respuesta = respuestasRepository.findByIdRespuesta(idRespuesta);
        List<Respuesta> respuestasList = topico.getRespuestaList();
        for (Respuesta respuestas: respuestasList){
            if(respuestas.getIdRespuesta().equals(idRespuesta)){
                return respuesta;
            }
        }
        return null;
    }

    public Respuesta findRespuesta(Long id){
        return respuestasRepository.findByIdRespuesta(id);
    }

    public void desmarcarOtrasRespuestasComoSolucion(Topico topico, Long idRespuestaActual) {
        List<Respuesta> respuestas = topico.getRespuestaList();
        for (Respuesta respuesta: respuestas){
            if(!respuesta.getIdRespuesta().equals(idRespuestaActual)){
                respuesta.setSolucion(false);
                respuestasRepository.save(respuesta);
            }
        }
    }

    public void updateRespuesta(Respuesta respuesta) {
        respuestasRepository.save(respuesta);
    }

    public void deleteRespuesta(Long id) {
        respuestasRepository.deleteById(id);
    }
}
