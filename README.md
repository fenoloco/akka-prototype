akka-prototype
==============

Prototipo para evaluar la factibilidad de usar Akka.

- ###Fase 1 (Modo Standalone)###

  - Implementar una interfaz para recibir mensajes desde el exterior. Similar a la existente para recibir los pedidos de las terminales/pos.
  - Implementar un servicio que actue de Mock de un Acquirer/procesadora.
  - Implementar 3 Actores internos para realizar lo siguiente:
    - Alterar el mensaje de entrada a un formato interno.
    - Alterar el mensaje de respuesta de servicio externo al mensaje interno.
    - Realizar algun tipo de procesamiento. En futura etapas este actor sera encargado de realizar las acciones en BD.

- ###Fase 2 (Modo Standalone conexion a BD.)###
  - Agregar a la Fase 1 conexio a una BD relacional
  - Investigar si requerimos que sea relacional o podriamos usar NoSQL
  
- ###Fase 3 (Modo Distribuido conexion a BD.)###
  - Implementar una distribuicion similar a lo planteado en esta aplicacion de ejemplo http://typesafe.com/activator/template/akka-distributed-workers para todo lo definido en las fases anteriores.

- ###Fase 4 (Evaluar lo implementado)###
  - Realizar una evaluacion respecto de los siguientes puntos.
    - Performance
    - Alta disponibilidad
    - Mantenibilidad
    - Productividad
    - Correctitud

- ###Fase N###
A DEFINIR
