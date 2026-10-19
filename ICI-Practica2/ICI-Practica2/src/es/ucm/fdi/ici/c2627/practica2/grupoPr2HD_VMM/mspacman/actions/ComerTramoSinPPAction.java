package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.PacManHelper;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import java.util.*;

public class ComerTramoSinPPAction implements Action 
{

    private double pill_falloff = 0.9;
    private double[] pillMap;
    private int[][] vecinos;
    
    private int[] cola;
    private double[] valor;
    private boolean[] visto;
    private int ini, fin;
    private boolean is_map_initialized = false;

    public ComerTramoSinPPAction() 
    {

    }

    @Override
    public MOVE execute(Game game) 
    {
        int pacman_nodo = game.getPacmanCurrentNodeIndex();
        MOVE pacman_last_move = game.getPacmanLastMoveMade();

        if (!game.isJunction(pacman_nodo)) 
        {
            return MOVE.NEUTRAL;
        }


        reservar_mapa(game);
        if (!is_map_initialized) {
        }


        calculate_pills_map(game);

        
        MOVE mejor_movimiento = MOVE.NEUTRAL;
        double mejor_valor = Double.NEGATIVE_INFINITY;

        MOVE[] movimientos_posibles = game.getPossibleMoves(pacman_nodo, pacman_last_move);
        for (MOVE m : movimientos_posibles) 
        {
            double v = valorar_tramo_y_futuro(game, pacman_nodo, m);
            if (v > mejor_valor) {
                mejor_valor = v;
                mejor_movimiento = m;
            }
        }

        return mejor_movimiento;
    }

    private void reservar_mapa(Game game) 
    {
        int n = game.getNumberOfNodes();
        pillMap = new double[n];
        vecinos = new int[n][];
        for (int i = 0; i < n; i++) {
            vecinos[i] = game.getNeighbouringNodes(i);
        }
        valor = new double[n];
        visto = new boolean[n];
        cola = new int[n];
        is_map_initialized = true;
    }

    private void calculate_pills_map(Game game) 
    {
        Arrays.fill(pillMap, 0);
        empezar();
        
        for (int p : game.getActivePillsIndices()) 
        {
            if (game.getPowerPillIndex(p) == -1) 
            {
                sembrar(p, 1.0);
            }
        }
        propagar(pillMap, pill_falloff);
        normalizar(pillMap);
    }

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
            for (int v : vecinos[u]) 
            {
                sembrar(v, valor[u] * caida);
            }
        }
    }

    private void normalizar(double[] capa) {
        double max = 0;
        for (double x : capa) max = Math.max(max, x);
        if (max > 0) {
            for (int i = 0; i < capa.length; i++) capa[i] /= max;
        }
    }

    // Adaptado de la practica 1, pero en vez de peor valor aqui hacemos la suma, porque nos interesa ver que lado hay mas pildoras normales
    private double valorar_tramo_y_futuro(Game game, int desde, MOVE m) {
        int anterior = desde;
        int n = game.getNeighbour(desde, m);
        double valor_acumulado = 0.0;

        while (n != -1 && !game.isJunction(n)) 
        {
            valor_acumulado += pillMap[n];
            int[] vecinos_nodo = vecinos[n];
            if(vecinos_nodo == null || vecinos_nodo.length == 0) break;
            
            int siguiente = (vecinos_nodo[0] == anterior && vecinos_nodo.length > 1) ? vecinos_nodo[1] : vecinos_nodo[0];
            anterior = n;
            n = siguiente;
        }

        if (n != -1) {
            valor_acumulado += pillMap[n];
        }
        
        return valor_acumulado;
    }

    @Override
    public String getActionId() {
        return "Comer Tramo Sin PP (Mapa Influencia)";
    }
}