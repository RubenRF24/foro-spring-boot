UPDATE topicos t
SET t.autor = (select u.id_usuario
FROM usuarios u
WHERE u.login = t.autor);

ALTER TABLE topicos CHANGE `autor` `autor` BIGINT(20);
ALTER TABLE topicos ADD FOREIGN KEY (autor) REFERENCES usuarios(id_usuario);
