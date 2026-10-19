package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import java.util.Arrays;

public class HuirPerseguirPildoraAction_v1 implements Action 
{

    private double danger_weight = 2.0; 
    private double danger_falloff = 0.7;
    
    private double pill_weight = 1.0;
    private double pill_falloff = 0.9;

    private double[] dangerMap;
    private double[] pillMap;
    private int[][] vecinos;
    
    private int[] cola;
    private double[] valor;
    private boolean[] visto;
    private int ini, fin;
    private boolean is_map_initialized = false;

    public HuirPerseguirPildoraAction_v1() {}

    @Override
    public MOVE execute(Game game) {
        int pacman_nodo = game.getPacmanCurrentNodeIndex();
        MOVE pacman_last_move = game.getPacmanLastMoveMade();

        if (!game.isJunction(pacman_nodo)) {
            MOVE[] posibles = game.getPossibleMoves(pacman_nodo, pacman_last_move);
            return posibles.length > 0 ? posibles[0] : MOVE.NEUTRAL;
        }

        MsPacManInput input = new MsPacManInput(game);

        reservar_mapa(game);
        calcular_mapa_peligro(game, input);
        calcular_mapa_pildoras(game);

        MOVE mejor_movimiento = MOVE.NEUTRAL;
        double mejor_valor = Double.NEGATIVE_INFINITY;

        for (MOVE m : game.getPossibleMoves(pacman_nodo, pacman_last_move)) 
        {
            double v = valorar_tramo(game, pacman_nodo, m);
            
            if (v > mejor_valor) 
            {
                mejor_valor = v;
                mejor_movimiento = m;
            }
        }

        return mejor_movimiento;
    }


    private void reservar_mapa(Game game) 
    {
        int n = game.getNumberOfNodes();
        dangerMap = new double[n];
        pillMap = new double[n];
        vecinos = new int[n][];
        for (int i = 0; i < n; i++) 
        {
            vecinos[i] = game.getNeighbouringNodes(i);
        }
        valor = new double[n];
        visto = new boolean[n];
        cola = new int[n];
        is_map_initialized = true;
    }

    private void calcular_mapa_peligro(Game game, MsPacManInput input) 
    {
        Arrays.fill(dangerMap, 0);
        empezar();
        
        for (GHOST g : GHOST.values()) 
        {
            if (!input.is_eatable(g) && game.getGhostLairTime(g) == 0) 
            {
                int nodo_fantasma = input.ghost_nodo_actual.get(g);
                sembrar(nodo_fantasma, 1.0);
            }
        }
        propagar(dangerMap, danger_falloff);
        normalizar(dangerMap);
    }

    private void calcular_mapa_pildoras(Game game) 
    {
        Arrays.fill(pillMap, 0);
        empezar();
        
        for (int p : game.getActivePillsIndices()) {
            sembrar(p, 1.0);
        }
        for (int pp : game.getActivePowerPillsIndices()) 
        {
        
            sembrar(pp, 2.0);
        }
        propagar(pillMap,pill_falloff);
        normalizar(pillMap);
    }

    private void empezar() {
        Arrays.fill(visto, false);
        ini = 0; fin = 0;
    }

    private void sembrar(int nodo, double v) 
    {
        if (!visto[nodo]) {
            visto[nodo] = true;
            valor[nodo] = v;
            cola[fin++] = nodo;
        }
    }

    private void propagar(double[] capa, double caida) 
    {
        while (ini < fin) {
            int u = cola[ini++];
            capa[u] += valor[u];
            for (int v : vecinos[u]) {
                sembrar(v, valor[u] * caida);
            }
        }
    }

    private void normalizar(double[] capa) 
    {
        double max = 0;
        for (double x : capa) max = Math.max(max, x);
        if (max > 0) {
            for (int i = 0; i < capa.length; i++) capa[i] /= max;
        }
    }

    private double utilidad(int nodo) 
    {
        return (pill_weight * pillMap[nodo]) - (danger_weight * dangerMap[nodo]);
    }

    // busca el peor valor
    private double valorar_tramo(Game game, int desde, MOVE m) 
    {
        int anterior = desde;
        int n = game.getNeighbour(desde, m);
        double peor_valor = Double.POSITIVE_INFINITY;
        
        while (n != -1) {
            double u = utilidad(n);
            if (u < peor_valor)
            {
                peor_valor = u;
            }
            
            if (game.isJunction(n))
            {
                break;
            }
            
            int[] vecinos_nodo = vecinos[n];
            if(vecinos_nodo == null || vecinos_nodo.length == 0) break;
            
            int siguiente = (vecinos_nodo[0] == anterior && vecinos_nodo.length > 1) ? vecinos_nodo[1] : vecinos_nodo[0];
            anterior = n;
            n = siguiente;
        }
        return peor_valor;
    }

    @Override
    public String getActionId() 
    {
        return "Huir (Mapa de Influencia)";
    }
}
