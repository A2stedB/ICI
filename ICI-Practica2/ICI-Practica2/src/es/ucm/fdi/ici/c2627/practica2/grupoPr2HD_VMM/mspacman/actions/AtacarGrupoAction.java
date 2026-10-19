package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;
import pacman.game.Game;
import java.util.Arrays;

public class AtacarGrupoAction implements Action 
{
    private double eatable_falloff = 0.8;
    private double[] eatableMap;
    private int[][] vecinos;
    
    private int[] cola;
    private double[] valor;
    private boolean[] visto;
    private int ini, fin;
    // private boolean is_map_initialized = false;

    public AtacarGrupoAction() {}

    @Override
    public MOVE execute(Game game) {
        int pacman_nodo = game.getPacmanCurrentNodeIndex();
        MOVE pacman_last_move = game.getPacmanLastMoveMade();

        if (!game.isJunction(pacman_nodo)) 
        {
            MOVE[] posibles = game.getPossibleMoves(pacman_nodo, pacman_last_move);
            return posibles.length > 0 ? posibles[0] : MOVE.NEUTRAL;
        }

        reservar_mapa(game);
        Arrays.fill(eatableMap, 0);
        empezar();
        for (GHOST g : GHOST.values()) 
        {
            if (game.isGhostEdible(g)) 
            {
                int ghost_node = game.getGhostCurrentNodeIndex(g);
                sembrar(ghost_node, 1.0);
            }
        }
        propagar(eatableMap, eatable_falloff);

        MOVE mejor_movimiento = MOVE.NEUTRAL;
        double mejor_valor = Double.NEGATIVE_INFINITY;

        for (MOVE m : game.getPossibleMoves(pacman_nodo, pacman_last_move)) 
        {
            int n = game.getNeighbour(pacman_nodo, m);
            double v = eatableMap[n];
            
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
        eatableMap = new double[n];
        vecinos = new int[n][];
        for (int i = 0; i < n; i++) vecinos[i] = game.getNeighbouringNodes(i);
        valor = new double[n];
        visto = new boolean[n];
        cola = new int[n];
        // is_map_initialized = true;
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

    private void propagar(double[] capa, double caida) 
    {
        while (ini < fin) 
        {
            int u = cola[ini++];
            capa[u] += valor[u];
            for (int v : vecinos[u]) sembrar(v, valor[u] * caida);
        }
    }

    @Override
    public String getActionId() {
        return "Atacar Hacia el Grupo";
    }
}
