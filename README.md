# Foro con spring boot
En este proyecto desarrolle una API Restful para un foro usando el lenguaje de JAVA con ayuda del framework de Spring boot.
Para la seguridad de la API tambien se hizo uso de JWT. 

Usando el modelo MVC, tendremos cuatro principales rutas:

## Ruta /topicos:

Dentro de esta ruta se hace manejo del metodo POST para crear los topicos, donde el titulo y el mensaje de la pregunta deben ser unicos.

Por otro lado haciendo uso del metodo GET, disponemos de dos opciones usar la ruta /topico para obtener un listado de todos los topicos realizados en el foro y la ruta /topicos/idTopico para obtener los datos de un topico en especifico.
Dentro de la informacion del topico visualizaremos el titulo, mensaje, la fecha de creacion, autor, curso y una lista con las respuesta que han escrito otros usuarios en este topico.

Metodo PUT: se usa en la ruta /topicos/idTopico donde se puede cambiar el titulo, mensaje y el curso del topico.

Si usamos la ruta /topicos/idTopico/cerrar-topico se realizaba un set al atributo cerrado cambiandolo de false a True del Topico y se actualiza el estatus a CERRADO.

Metodo DELETE: nos permite borrar un topico y todos sus descendientes usando la ruta /topicos/idTopico.

## Ruta /usuarios:

Metodo POST: Lo usamos para registrar un nuevo usuario dentro del foro en la ruta /usuarios donde se requeriran un usuario, email y una clave. El usuario y el email deben ser unicos en el foro.

METODO GET: Usando la ruta /usuarios nos regresa un listado con todos los usuarios que estan registrado en nuestro foro, mostrando su usuario y email.

Usando la ruta /usuarios/nombreUsuario retorna los datos del usuario y una lista de todos los topicos que ha creado este usuario.

METODO PUT: Dirigiendonos a la ruta /usuarios/idUsuario podremos modificar todos los campos mientras sigan cumpliendo con los requerimientos que hay al momento de registrarse. Tambien el usuario que desea modificar los datos debe ser el mismo que se encuentre logeado.

METODO DELETE: Con la ruta /usuarios/idUsuario podremos borrar el usuario verificando que el usuario logeado sea el mismo que intenta borrarlo.

## Ruta /cursos:

Metodo POST: Accediendo a la ruta /cursos, podremos crear un curso pasando el nombre y la categoria del curso. El nombre debe ser unico

Metodo GET: Usando la ruta /cursos nos retorna una lista con todos los cursos creados.

Si usamos la ruta /cursos/nombreCurso nos retorna los datos de dicho curso y una lista con todos los topicos relacionados a ese curso.

Metodo PUT: Si accedemos a la ruta /cursos/nombreCurso y usamos este metodo, podemos modificar el nombre o la categoria del curso elegido.

Metodo DELETE: Accediendo a la ruta /cursos/nombreCurso podremos eliminar completamente un curso.

## Ruta /respuestas:

Metodo POST: En la ruta /respuestas/idTopico creamos una respuesta para ese topico introduciendo el mensaje que se desea publicar.

Metodo PUT: Con la ruta /respuestas/idTopico/idRespuesta/marcar-solucion, se verificara si la persona logeada es el autor del topico, si se logra verificar correctamente se recorrera la lista de respuestas del topico introducido buscando el id de la respuesta brindada en la ruta, si esto es exitoso, si hay una respuesta que ya ha sido marcada como solucion se desmarcara al terminar esto se hara un set true al atributo solucion de la Respuesta y el topico cambiara su estatus a SOLUCIONADO si no lo estaba antes.

Metodo DELETE: En la ruta /respuestas/idRespuesta y se verificara si el usuario logeado es el autor del topico o de la Respuesta, si es exitoso se verificara si la respuesta ha sido marcada como solucion, si esto es verdadero el topico cambiara su estatus a de SOLUCIONADO a NO_SOLUCIONADO y la respuesta sera eliminada.
