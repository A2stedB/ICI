package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Game;

public class ComerLimpiarNivelAction implements Action {

    public ComerLimpiarNivelAction() 
    {

    }

    @Override
    public MOVE execute(Game game) {
        int pacman_nodo = game.getPacmanCurrentNodeIndex();
        MOVE pacman_last_move = game.getPacmanLastMoveMade();

        if (!game.isJunction(pacman_nodo)) 
        {
            return MOVE.NEUTRAL;
        }

        int[] pildoras_normales = game.getActivePillsIndices();
        int[] pildoras_poder = game.getActivePowerPillsIndices();

        int target_node = -1;
        double min_distance = Double.MAX_VALUE;

        if (pildoras_normales.length > 0) {
            int closest_normal = game.getClosestNodeIndexFromNodeIndex(pacman_nodo, pildoras_normales, DM.PATH);
            if (closest_normal != -1) 
            {
                double dist = game.getShortestPathDistance(pacman_nodo, closest_normal, pacman_last_move);
                if (dist < min_distance) 
                {
                    min_distance = dist;
                    target_node = closest_normal;
                }
            }
        }

        if (pildoras_poder.length > 0) // Puede pasar
        {
            int closest_power = game.getClosestNodeIndexFromNodeIndex(pacman_nodo, pildoras_poder, DM.PATH);
            if (closest_power != -1) {
                double dist = game.getShortestPathDistance(pacman_nodo, closest_power, pacman_last_move);
                if (dist < min_distance) 
                {
                    min_distance = dist;
                    target_node = closest_power;
                }
            }
        }

        if (target_node != -1) {
            MOVE move_hacia_restos = game.getNextMoveTowardsTarget(pacman_nodo, target_node, pacman_last_move, DM.PATH);
            if (move_hacia_restos != null) 
            {
                return move_hacia_restos;
            }
        }

        return MOVE.NEUTRAL;
    }

    @Override
    public String getActionId() {
        return "Limpiar Nivel (Ir a píldora más cercana)";
    }
}
