El movimiento del MsPacMan se basa en una busqueda del mejor camin basado en un mapa de peso
El mapa peso final esta calculado en base a 4 capas:
  - Pildoras normales
  - Pildoras de poder
  - Fantasma normal
  - Fantasma comestible

Una idea inicial era utilizar un mapa de peso junto con decisiones concretas. Al final utilize simplemente mapas de peso y no decisiones mas concretos con `if...else...` porque las decisiones chocaba. Los `if...else` actua mas como de "sentencia unica", mientras que los mapas de peso actua mas de dar opciones y luego decidir. Abandone esta idea porque una contradice la otra para mi, pero creo que desarrollando un poco mejor la idea de mezclar ambos puede llegar a mejores resultados

La idea en general es si sobrevivo, puedo conseguir mas puntuacion.

Las pildoras normales esta en una capa separada de las pildoras de poder porque considero que no pueden tratarlos de la misma manera, porque no es lo mismo si como una pildora de poder y empiezo la persecucion a los fantasmas comestibles y empezar a arriesgarme. Por eso tiene un peso y un falloff diferente que una pildora normal.

La misma idea es aplicable para los fantasmas,los fantasmas comestibles tiene mas falloff por temas de seguridad, aunque pueda conseguir mas puntos.

La idea era hacer que el peso de los fantasmas comestibles fuese dinamico en funcion del tiempo que les queda, pero al final solo me ha dado tiempo a implementar que fuese dinamico su valor inicial al calcular en el mapa

Haciendo que algunos datos fuese dinamicos creo que puede mejorar algo el comportamiento



## Ideas sueltas (de antes)

La idea es tener en todo el momento los datos de los fantasmas, y tomar una decision en funcion de los datos

Coger la distancia euclidea y el ultimo movimiento, en funcion de esos dos datos mover el pacman

La idea seria intentar pillar todas las pildoras normales, y si una fantasma esta a cierta distancia euclidea, ir por una pildora (puede que quede en bucle lol)

O otra idea es mantener siempre a una distancia euclidea de una pildora, marcar esos nodos como un zona, y desplazamos de zona en zona, pero esto es como complicarse la vida

Tener precalculado todos los nodos vecinos

Otra idea es hacer un mapa de peso con la posicion, el ultimo movimiento del fantasma, se puede hacer otra cosa que es combinar mirar a 1 paso por delante de los fantasmas, en su proxima interseccion hacer otro mapa de peso, no de todo el mapa, sino hasta las 2 siguientes intersecciones o a cierta distancia de nodos desde el punto donde parte la salida del mapa

(Que me aporta esto)

La informacion que necesita PacMan para tomar una decision la posicion del los fantasmas.

El objetivo del PacMan es consumir todas las pildoras.

Intersecciones adyacentes?
Intersecciones adyacentes dado la direccion;

Dar distinta prioridad, si hay una fantasma dentro de un radio a distancia euclidea, entonces tiene que "escapar".

- Escapar se entiende por 2 cosas: 1. Si hay una pildora de poder cerca, entonces recalcula y dirige hacia ella teniendo en cuenta los posibles intersecciones de los fantasmas 2. Si esta "encerrado", entonces busca una interseccion
  Si no hay fantasma cerca, entonces dar prioridad a comer las pildoras "cercanas"



