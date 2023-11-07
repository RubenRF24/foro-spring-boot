create table respuestas(

    id_respuesta bigint not null auto_increment,
    mensaje text not null unique,
    topico bigint not null,
    fecha_creacion date not null,
    autor bigint not null,
    solucion tinyint DEFAULT 0,

    primary key(id_respuesta)
);

ALTER TABLE respuestas ADD FOREIGN KEY (autor) REFERENCES usuarios(id_usuario);
ALTER TABLE respuestas ADD FOREIGN KEY (topico) REFERENCES topicos(id_topico);