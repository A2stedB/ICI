package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.actions;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class HuirPerseguirPildoraAction_v0 implements Action {

    private int margen_seguridad = 2;

    public HuirPerseguirPildoraAction_v0() {}

    @Override
    public MOVE execute(Game game) 
    {
        int pacman_nodo = game.getPacmanCurrentNodeIndex();
        MOVE pacman_last_move = game.getPacmanLastMoveMade();

        if (!game.isJunction(pacman_nodo)) {
            MOVE[] posibles = game.getPossibleMoves(pacman_nodo, pacman_last_move);
            return posibles.length > 0 ? posibles[0] : MOVE.NEUTRAL;
        }

        MsPacManInput input = new MsPacManInput(game);
        MOVE[] posibles_movimientos = game.getPossibleMoves(pacman_nodo, pacman_last_move);
        
        List<MOVE> movimientos_seguros = new ArrayList<>();

        for (MOVE m : posibles_movimientos) {
            if (is_movimiento_seguro(game, input, pacman_nodo, m)) {
                movimientos_seguros.add(m);
            }
        }

        if (movimientos_seguros.isEmpty()) {
            return fallback_huida_desesperada(game, input, pacman_nodo, pacman_last_move);
        }

       MOVE mejor_movimiento = movimientos_seguros.get(0);
        int distancia_minima = Integer.MAX_VALUE;
        
        int[] pildoras_normales = game.getActivePillsIndices();
        int[] pildoras_poder = game.getActivePowerPillsIndices();

        for (MOVE m : movimientos_seguros) 
        {
            int interseccion_destino = get_siguiente_interseccion(game, pacman_nodo, m);
            
            if (interseccion_destino != -1) 
            {
                if (pildoras_normales.length > 0) 
                {
                    int p_cercana = game.getClosestNodeIndexFromNodeIndex(interseccion_destino, pildoras_normales, DM.PATH);
                    if (p_cercana != -1) {
                        int dist = game.getShortestPathDistance(interseccion_destino, p_cercana, m);
                        if (dist != -1 && dist < distancia_minima) 
                        {
                            distancia_minima = dist;
                            mejor_movimiento = m;
                        }
                    }
                }

                if (pildoras_poder.length > 0) {
                    int pp_cercana = game.getClosestNodeIndexFromNodeIndex(interseccion_destino, pildoras_poder, DM.PATH);
                    if (pp_cercana != -1) 
                    {
                        int dist = game.getShortestPathDistance(interseccion_destino, pp_cercana, m);
                        if (dist != -1 && dist < distancia_minima)
                        {
                            distancia_minima = dist;
                            mejor_movimiento = m;
                        }
                    }
                }
            }
        }

        return mejor_movimiento;
    }
    
    private boolean is_movimiento_seguro(Game game, MsPacManInput input, int nodo_inicio, MOVE movimiento) 
    {
        int interseccion_destino = get_siguiente_interseccion(game, nodo_inicio, movimiento);
        if (interseccion_destino == -1) return false;

        int distancia_llegada_pacman = game.getShortestPathDistance(nodo_inicio, interseccion_destino, movimiento);

        for (GHOST ghost : GHOST.values()) {
            if (!input.is_eatable(ghost) && game.getGhostLairTime(ghost) == 0) {
                int ghost_node = input.ghost_nodo_actual.get(ghost);
                MOVE ghost_move = input.ghost_last_move.get(ghost);
                
                int distancia_llegada_fantasma = game.getShortestPathDistance(ghost_node, interseccion_destino, ghost_move);
                
                if (distancia_llegada_fantasma != -1 && distancia_llegada_fantasma <= distancia_llegada_pacman + margen_seguridad) 
                {
                    return false;
                }
            }
        }
        
        return true;
    }

    private int get_siguiente_interseccion(Game game, int nodo_inicio, MOVE movimiento) 
    {
        int actual = game.getNeighbour(nodo_inicio, movimiento);
        MOVE dir = movimiento;
        
        while (actual != -1 && !game.isJunction(actual)) 
        {
            MOVE[] posibles = game.getPossibleMoves(actual, dir);
            if (posibles.length == 0) return actual; 
            dir = posibles[0];
            actual = game.getNeighbour(actual, dir);
        }
        return actual;
    }

    private MOVE fallback_huida_desesperada(Game game, MsPacManInput input, int pacman_nodo, MOVE last_move) 
    {
        int closest_ghost_node = -1;
        int min_dist = Integer.MAX_VALUE;
        for (GHOST ghost : GHOST.values()) {
            if (!input.is_eatable(ghost) && game.getGhostLairTime(ghost) == 0) 
            {
                int dist = game.getShortestPathDistance(input.pacman_nodo, input.ghost_nodo_actual.get(ghost), input.pacman_last_move);
                if (dist != -1 && dist < min_dist) 
                {
                    min_dist = dist;
                    closest_ghost_node = input.ghost_nodo_actual.get(ghost);
                }
            }
        }
        if (closest_ghost_node != -1) {
            return game.getNextMoveAwayFromTarget(pacman_nodo, closest_ghost_node, last_move, DM.PATH);
        }
        return game.getPossibleMoves(pacman_nodo, last_move)[0];
    }

    @Override
    public String getActionId() {
        return "Huir Comiendo Global";
    }
}