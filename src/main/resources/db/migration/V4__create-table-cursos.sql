create table cursos(

    id_curso bigint not null auto_increment,
    nombre varchar(100) not null unique,
    categoria varchar(300) not null,

    primary key(id_curso)

);