package es.ucm.fdi.ici.c2627.practica1.grupoPr1HD_VMM;

import pacman.game.Constants.*;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;

import java.util.Arrays;

public class MsPacManMap_v1 
{
    public double pill_weight = 1;
    public double power_pill_weight = 1;
    public double danger_ghost_weight = 1.7;
    public double eatable_ghost_weight = 2;

    public double pill_falloff = 0.9;
    public double power_pill_falloff = 0.7;
    public double danger_falloff = 0.9;
    public double eatable_falloff = 0.7;

    public double[] pillMap, powerPillMap, dangerMap, eatableGhostMap;

    public int[][] vecinos;

    public int[] cola;
    public double[] valor;            // valor con el que entra cada nodo en la cola
    public boolean[] visto;
    public int ini, fin;

    float GHOST_DANGE_INFLUENCE_DISTANCE = 50;


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
        for (int p : game.getActivePowerPillsIndices()) 
        {
            double value = calculateGhostInfluence(game, p);
            sembrar(p, value);
        }
        propagar(powerPillMap, power_pill_falloff);
        normalizar(powerPillMap);
    }

    // Funcion que devuelve el valor 
    // Si la distancia entre el pacman y fantasma es una distancia superior a la de GHOST_DANGE_INFLUENCE_DISTANCE, decimos que esta "seguro"
    // y le bajamos el peso
    private double calculateGhostInfluence(Game game,int pill_position)
    {
        double value = 0;
        double closest_distance = Float.MAX_VALUE;
        int pacman_node_index = game.getPacmanCurrentNodeIndex();
        for(GHOST ghost : GHOST.values())
        {
            int ghost_node_index = game.getGhostCurrentNodeIndex(ghost);

            if(ghost_node_index == 1292 || !game.isGhostEdible(ghost))
            {
                continue;
            }
            MOVE ghost_last_move = game.getGhostLastMoveMade(ghost);
            double distance = game.getShortestPathDistance(ghost_node_index, pacman_node_index, ghost_last_move);

            // double x = GHOST_DANGE_INFLUENCE_DISTANCE / distance;
            if(distance < closest_distance) // coger directamente el valor pq la media aqui puede variar mucho
            {
                closest_distance = distance;
            }
        }

        // System.out.println("Distancia del fantasma mas cencana: " + closest_distance);
        if(closest_distance > GHOST_DANGE_INFLUENCE_DISTANCE || closest_distance == 0) return 0.7;
        
        value = GHOST_DANGE_INFLUENCE_DISTANCE / closest_distance;
        // System.out.println("Valor de la pildora "+ pill_position + " vale " + value);

        // if(closest_distance < 0.0)
        // {
        //     return 1 - closest_distance;
        // }

        // System.out.println("Peligro cerca!!!");

        return value;
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
        int pacman_node_index = game.getPacmanCurrentNodeIndex();
        empezar();
        int distance = game.getShortestPathDistance(pacman_node_index, ghost_index, game.getGhostLastMoveMade(type));

        if(distance / 2 < time){ //  1 paso por tick, si le da tiempo、
            // int[] pos = new int[1];
            // pos[0] = ghost_index;
            sembrar(ghost_index, start_value);
            propagar(eatableGhostMap, eatable_falloff);

            // Por algun motivo no me deja llamar a esta funcion....
            // GameView.addPoints(game, Color.RED, ghost_index);
        }
        else{
            sembrar(ghost_index, 0);
            propagar(eatableGhostMap, eatable_falloff);
        }


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
