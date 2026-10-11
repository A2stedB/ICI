
# Diario Practica 2
- Autor: He Deng
- Grupo: HD_VMM


## Domingo 11-10-2026 : Diseño v0 terminado

- IA : No

Las acciones del PacMan son:
- Dirigir hacia una pildora en una posicion concreta (equivale a comer una pildora)
- Dirigir hacian una pildora de poder en una posicion concreta
- Ir a una interseccion concreta
- Comer una presa
- Dirigir hacia una posicion concreta

Al final lo de si es seguro o no eso es muy relativo, pero en los calculos se tiene en cuenta para realizar la accion correspondiente.

Leyendo la seccion que habla sobre FSM y HSM del libro de *AI for Games* me he dado cuenta de una cosa, y es que utilizando mal la idea de tener jerarquia se puede complicar la resolucion del problema sin necesidad. Rollo no es un silver bullet esta cosa. HSM lo considero mas como una forma de agrupar las cosas con semejanza, pero es realmente una buena forma?

Tambien me ha gustado la idea de testear las condiciones, asi se puede reutilizar las cosas, quizas sea interesante implementarlo en la practica.

El primer diseño parte de la idea de que hay dos estados claros del juego,las pildoras de poder activo y no activo, y en cada estado hay decisiones diferentes. La idea es estructrcturarlo de manera simple, y luego si veo que es necesario añadir estados se puede hacerlo de manera muy simple.



## Jueves 8-10-2026

- IA : No

Reflexionando,pensando en como modelar los comportamientos del PacMan en "estados"

Las preguntas que me he hecho:
- Como modelar el comportamiento en forma de "Estado"?
- Como el pacman "percibe" el entorno?
- Como convertir nuestra intuicion en un "estado"? Porque nosotros como humanos a la hora de jugar no jugamos de forma pensando en "vale y si pasa esto hago esto, y si pasa otro hago lo otro" asi constantemente
- La cadena de decision son estados y transiciones?

Lo que estoy pensando se parece mas a un arbol de decisiones que una maquina de estado, por como estoy interpretando y modelando el problema.

Interpretar los estados de pacman como un especie de "sensacion"

Partir de la idea de que el juego esta formado por 2 estados: cuando ha comido una pildora de poder y cuando no esta activo la pildora de poder