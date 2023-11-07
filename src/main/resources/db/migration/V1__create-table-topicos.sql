create table topicos(
    id_topico bigint not null auto_increment,
    titulo varchar(200) not null unique,
    mensaje text not null unique,
    fecha_creacion date not null,
    estatus varchar(100) not null,
    autor varchar(100) not null,
    curso varchar(100) not null,

    primary key(id_topico)

);