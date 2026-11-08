
# v0



Numero de partidas para las 3 fantasmas: 5000

|            | Media    | Desviacion tipica | Error estandar |
| ---------- | -------- | ----------------- | -------------- |
| Basico     | 5218.884 | 2366.3            | 33.4           |
| Intermedio | 3483.512 | 1621.1            | 22.9           |
| Avanzado   | 2499.668 | 941.354           | 13.3           |
| Media      | 3734     | 1642              | 23.2           |

observacion: El arreglo del Helper.PACMAN_get_closest_power_pill no afecta en las estadisticas obtenidas del Pacman

# v1

La capa de la pildora de poder implementado con una influencia dinamica en funcion de los fantasmas que tiene cerca., Y tambien la capa de comestible con influencia dinamica en funcion del tiempo que les queda y la distancia hasta ellos.

5000 partidas simuladas para los 3 tipos de fantasma

|            | Media    | Desviacion tipica | Error estandar |
| ---------- | -------- | ----------------- | -------------- |
| Basico     | 5269.812 | 2344.8            | 33.1           |
| Intermedio | 3506.844 | 1606.1            | 22.7           |
| Avanzado   | 2493.948 | 905.3             | 12.8           |
| Media      | 3756     | 1618              | 22.8           |
Observando graficamente una partida puntual noto que el pacman es mas "tonto" a la hora elegir por que camino ir.

Conclusion: No aporta una mejora significativa.