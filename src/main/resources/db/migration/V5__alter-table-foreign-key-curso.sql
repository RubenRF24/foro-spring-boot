UPDATE topicos t
SET t.curso = (select c.id_curso
FROM cursos c
WHERE c.nombre = t.curso);

ALTER TABLE topicos CHANGE `curso` `curso` BIGINT(20);
ALTER TABLE topicos ADD FOREIGN KEY (curso) REFERENCES cursos(id_curso);
