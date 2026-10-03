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
