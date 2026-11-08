# Reentrega

## La misma vara

Las pruebas de MsPacmanV0 y MsPacManV1 contra los mismos rivales:

Numero de partidas: 5000

MsPacManV0

| Rival\Estadistica |  Media   | Desviacion tipica | Error estandar |
| :---------------: | :------: | :---------------: | :------------: |
|      Basico       | 3032.396 |      1211.83      |     17.13      |
|    Intermedio     | 2301.268 |      1027.06      |     14.52      |
|     Avanzado      | 1796.332 |      647.00       |      9.15      |
|                   |          |                   |                |
|       Media       | 2376.666 |    961.9633333    |      13.6      |
MsPacManV1

| Rival\Estadistica |    Media    | Desviacion tipica | Error estandar |
| :---------------: | :---------: | :---------------: | :------------: |
|      Basico       |  4163.674   |      1988.95      |     28.12      |
|    Intermedio     |   3063.02   |      1415.2       |     20.01      |
|     Avanzado      |   2278.01   |      780.60       |     11.03      |
|                   |             |                   |                |
|       Media       | 3168.234667 |    1394.916667    |     19.72      |

vale lo he probado el `Comparar` que viene en la plantilla y alli indica mejor los datos, aunque de alli no puedo sacar la desviacion tipica y el error estandar

El comparar se ha hecho con 5000 partidas tambien

```
== puntos: MsPacmanV1 frente a MsPacmanV0 (más es mejor) ==
rival                MsPacmanV0       MsPacmanV1         D   2·ET_D
Random                3381 ± 18        5913 ± 27     +2533       66  mejor
Aggressive            3842 ± 28        7412 ± 60     +3571      133  mejor
Basico                3056 ± 17        4207 ± 28     +1152       66  mejor
Intermedio            2297 ± 14        3084 ± 20      +787       49  mejor
Avanzado               1805 ± 9        2268 ± 11      +463       29  mejor
------------------------------------------------------------------------
MEDIA (5)              2876 ± 8        4577 ± 15     +1701       34  mejor
MsPacmanV1 es mejor contra 5 rivales, peor contra 0 y no se distingue contra 0.
```

Solo me interesa la metrica de puntos


Efectivamente el `MsPacManV1`, con `HuirPerseguirPildoraAction_v1` que funciona por mapa de influencia, funciona mejor.

Lo que pasaba es que en `HuirPerseguirPildoraAction_v0` se tomaba decisiones muy puntuales, no llegaba mas alla de una interseccion, en cambio en la `v1`, al tener la mapa de influencia, lo interesante que es ir por un camino esta calculado en funcion de toda la informacion del mapa.

No he utilizado IA (Para que)

Me quedo con la version `v1`, porque funciona mejor en termino de conseguir puntos.


## La misma vara

Vale efectivamente se me paso de largo el `ComestibleLejosTransition` porque bueno, 4 de la mañana. Este estado es simplemente que dado una distancia que yo ponga, ver si un comestible esta dentro de esa distancia mas corta.

y lo de `SmallEatableTimeTransition(Constants.EDIBLE_TIME)` pues esta mal tambien, ese parametro es el tiempo que yo pongo como regla de decir cuando un comestible que queda poco tiempo. Y lo que ocurre es la condicion de `max_tiempo_restante < limite_tiempo` siempre devuelve `true` cuando esta en esa transicion, porque el multiplicador minimo del `EDIBLE_TIME` es `0.9` 

El comparar con 5000 partidas

```
== puntos: MsPacmanV2 frente a MsPacmanV1 (más es mejor) ==
rival                MsPacmanV1       MsPacmanV2         D   2·ET_D
Random                5926 ± 28        6271 ± 29      +345       81  mejor
Aggressive            7412 ± 60        7789 ± 66      +377      179  mejor
Basico                4202 ± 28        4664 ± 32      +463       86  mejor
Intermedio            3059 ± 20        3320 ± 23      +260       60  mejor
Avanzado              2268 ± 11        2467 ± 12      +199       33  mejor
------------------------------------------------------------------------
MEDIA (5)             4574 ± 15        4902 ± 17      +329       45  mejor
MsPacmanV2 es mejor contra 5 rivales, peor contra 0 y no se distingue contra 0.
```

El resultado de poner `ComestibleLejosTransition_v1` con parametro 40 nodos y  `SmallEatableTimeTransition_v1` con limite de tiempo a 60.

Tiene algo de mejora, no tanto como lo que habia entre `MsPacManV0` y `MsPacManV1`, pero algo es algo.

La mejora creo que es debido a que ahora de verdad mira bien el tiempo de verdad que le falta a los comestibles, y tambien mira si el comestible esta lejos, a una distancia alcanzable

No he utilizado IA (Para que de nuevo xD)

Me quedo con la version `MsPacManV2`, con las 2 mejoras implementadas, porque este es como deberia ser desde un principio xD, ahora tiene los comportamientos que yo me esperaba mas o menos de lo que hace la maquina de estado que diseñe.