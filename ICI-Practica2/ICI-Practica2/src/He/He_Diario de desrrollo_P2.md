
# Diario Practica 2
- Autor: He Deng
- Grupo: HD_VMM


## Domingo 19-10 y lunes a la madrugada / mañana

He tenido que hacer que pille pildoras explicitamente en el estado de huir, porque sino siempre esta huyendo de los fantasmas (independiente de si le sigue por detras o no), pero el objetivo es intentar pillar todas las pildoras.

Vale obviamente se quedaba dando vueltas porque no habia marcado las casillas exploradas como visitados en el DFS del `CaminoBloqueadoTransition` y `HuirBuscarSalidaAction`.

He probado otra cosa, y eso si que creo que cuenta como prueba, y es que en el estado HuirPerseguirPildora, en vez de utilizar `getShortestPathDistance` para ver la distancia a cada pildora, he cogido el mapa de influencia de la practica anterior, porque lo que podia pasar era que nunca pilla las ultimas pildoras. 

5000 partidas con mapa de influencia en la accion de HuirPerseguirPildora

|            |  Media   | Desviacion tipica | Error tipico |
| :--------: | :------: | :---------------: | :----------: |
| Intermedio | 3036.374 |      1405.3       |     19.8     |
Lo que se puede observar es que ha aumentado la media, pero tambien la desviacion tipica, eso quiere decir que hay mas dato atipicos, lo cual creo que se podria decir que esta version es menos "estable" aunque tenga mas media.

Me quedo con esta segunda version, porque viendolo como juega se siente que hace movimientos mas "razonables" que el primero

## Sabado 18-10 y Domingo 19-10

Hola Pablo, llevo aqui casi 20 horas despierto, entre otras cosas, y estoy escribiendo esto a las 5:36 AM.


Tire a la basura el diseño que propuse, porque le estuve dando unas cuantas vueltas, por una parte me seguia costando interpretar las acciones como "Estados", por otra parte es verdad que no tenia mucho sentido desde un principio, y en el review dijiste ciertas cosas que es vd que no se entendia.

Aunque es vd que antes de tirar a la basura ese diseño habria estado bien implementarlo, tomar medidas y luego ya mejorarlo o tirarlo, pero no me ha dado tiempo en la vida real.

Solo tengo una implementacion...

El segundo diseño me he centrado mas en la idea de "Si yo fuese PacMan en ese juego (pero real), que puedo hacer?". Y el resultado es lo que hay ahora. Tiene un estado inicial del cual parte, con un movimiento neutral. Y tiene 3 subestados jerarquicos, que son Comer, Atacar y huir, conectados mediante sus transiciones correspondientes.

El estado mas complejo probablemente es el de huir, porque tiene que tener en cuenta muchas cosas, que el camino no quede bloqueado por los fantasmas, que no quede infinitamente en este estado porque el 99% de tiempo va a estar en este estado, y aqui tiene que ser capaz de intentar ir a lugares donde hay pildoras.

En principio tiene 3 estados, Huir, buscar PP si hubiera, y en el caso de que el camino quede bloqueado, buscar una salida.


En la submaquina de Comer, la idea es mientras que estas cerca de un PP, intentar comer el mayor numerod de pildoras normales posible, asi para aprovecha el efecto que aporta el PP. Y cuando se ha consumido el PP mas cerca, ir al cercano del PP que esta mas cerca.
Y cuando queda pocas pildoras, lo que se hace es terminar de comer esas pildoras, para pasar del nivel.

En la submaquina de Atacar, es simplemente si hay comestible cerca ir a por el, y si estan juntos intentar comerlos todos.

En la submaquina de Huir, la idea es ir viendo por adelantado si los fantasmas pueden bloquear el camino de pacman. Si es posible, entonces buscar una forma de salir. Si esta huyendo normal, entonces intenta pillar las pildoras a medida que huye, que es la gran mayoria de casos.

Y me acabo de dar cuenta de que no he contado casi nada del proceso..., de nuevo solo he dicho lo que hay ahora...

En algunos casos se quedaba dando vueltas por una cosa que no consigo verlo de todo el estado de buscar salida...

|            |  Media   | Desviacion tipica | Error tipico |
| :--------: | :------: | :---------------: | :----------: |
| Intermedio | 2276.414 |      1004.8       |     14.2     


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