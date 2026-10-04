package es.ucm.fdi.ici.c2627.practica1.grupoXY;

import pacman.game.Constants.*;
import pacman.game.Constants;
import pacman.game.Game;
import java.util.Arrays;

public class MsPacManMap 
{
    public double pill_weight = 1;
    public double power_pill_weight = 1.2;
    public double danger_ghost_weight = 1.7;
    public double eatable_ghost_weight = 2;

    public double pill_falloff = 0.9;
    public double power_pill_falloff = 0.7;
    public double danger_falloff = 0.9;
    public double eatable_falloff = 0.7;

    public double[] pillMap, powerPillMap, dangerMap, eatableGhostMap;

    public int[][] vecinos;

    // Comun para los mapas
    public static int[] cola;
    public static double[] valor;            // valor con el que entra cada nodo en la cola
    public static boolean[] visto;
    public static int ini, fin;


    // El orden es:
    // Saber tus precondiciones de cada mapa, "resetear" el mapa de peso correspondiente(empezar()) 
    // y dependiendo de lo que quieres pues lo vamos sembrando, y al final lo normalizamos

    public void calculateMap(Game game)
    {
        calculatePillsMap(game);
        calculateGhostMap(game);
    }

    private void calculatePillsMap(Game game)
    {
        Arrays.fill(pillMap, 0);

        empezar();
        for (int p : game.getActivePillsIndices()){
            sembrar(p, 1.0);
        }
        propagar(pillMap, pill_falloff);
        normalizar(pillMap);

        Arrays.fill(powerPillMap, 0);
        empezar();
        for (int p : game.getActivePowerPillsIndices()) {
            sembrar(p, 1.0);
        }
        propagar(powerPillMap, power_pill_falloff);
        normalizar(powerPillMap);
    }

    private void calculateGhostMap(Game game)
    {
        Arrays.fill(dangerMap, 0);
        Arrays.fill(eatableGhostMap, 0);

        for (GHOST g : GHOST.values()) {
            if (game.getGhostLairTime(g) > 0) {
                continue;                          // en la cárcel: no está en el laberinto
            }
            int nodo = game.getGhostCurrentNodeIndex(g);
            if (game.isGhostEdible(g)) 
            {
                calculateEatableGhostMap(game,g,nodo);
            } 
            else 
            {
                // No puede darse la vuelta: la primera ola sólo sale hacia delante
                empezar();
                visto[nodo] = true;
                dangerMap[nodo] += 1.0;
                int[] delante = game.getNeighbouringNodes(nodo, game.getGhostLastMoveMade(g));
                // int[] delante = vecinos[nodo];
                if (delante == null) {
                    delante = vecinos[nodo];       // sin sentido conocido: hacia todos lados
                }
                for (int v : delante) {
                    sembrar(v, danger_falloff);
                }
                propagar(dangerMap, danger_falloff);
            }
        }
        normalizar(dangerMap);
        normalizar(eatableGhostMap);
    }

    private void calculateEatableGhostMap(Game game,GHOST type,int ghost_index)
    {
        float time = game.getGhostEdibleTime(type);
        // int pacman_node = game.getPacmanCurrentNodeIndex();

        double start_value = time / Constants.EDIBLE_TIME;
        empezar();
        sembrar(ghost_index, start_value);
        propagar(eatableGhostMap, eatable_falloff);
    }


    public void reservar(Game game) {
        // laberinto = game.getMazeIndex();
        int n = game.getNumberOfNodes();
        powerPillMap = new double[n];
        pillMap  = new double[n];
        dangerMap = new double[n];
        eatableGhostMap   = new double[n];
        vecinos = new int[n][];
        for (int i = 0; i < n; i++) {
            vecinos[i] = game.getNeighbouringNodes(i);
        }

        valor   = new double[n];
        visto   = new boolean[n];
        cola    = new int[n];
    }

    // public static void calculate

    private void empezar() {
        Arrays.fill(visto, false);
        ini = 0;
        fin = 0;
    }

    private void sembrar(int nodo, double v) {
        if (!visto[nodo]) {
            visto[nodo] = true;
            valor[nodo] = v;
            cola[fin++] = nodo;
        }
    }

    private void propagar(double[] capa, double caida) {
        while (ini < fin) {
            int u = cola[ini++];
            capa[u] += valor[u];
            for (int v : vecinos[u]) {
                sembrar(v, valor[u] * caida);
            }
        }
    }

    private static void normalizar(double[] capa) {
        double max = 0;
        for (double x : capa) max = Math.max(max, x);
        if (max > 0) {
            for (int i = 0; i < capa.length; i++) capa[i] /= max;
        }
    }
}
