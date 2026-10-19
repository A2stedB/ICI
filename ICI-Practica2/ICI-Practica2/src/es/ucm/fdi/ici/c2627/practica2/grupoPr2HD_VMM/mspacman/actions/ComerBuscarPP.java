package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Game;

public class ComerBuscarPP implements Action {

    public ComerBuscarPP() 
    {

    }

    @Override
    public MOVE execute(Game game) {
        int pacman_nodo = game.getPacmanCurrentNodeIndex();
        MOVE pacman_last_move = game.getPacmanLastMoveMade();

        if (!game.isJunction(pacman_nodo)) {
            MOVE[] posibles = game.getPossibleMoves(pacman_nodo, pacman_last_move);
            if (posibles.length > 0) return posibles[0];
            return MOVE.NEUTRAL;
        }

        int[] power_pills = game.getActivePowerPillsIndices();
        
        if (power_pills.length == 0) {
            return MOVE.NEUTRAL;
        }

        int closest_pp = game.getClosestNodeIndexFromNodeIndex(pacman_nodo, power_pills, DM.PATH); //......

        MOVE move_hacia_pp = game.getNextMoveTowardsTarget(pacman_nodo, closest_pp, pacman_last_move, DM.PATH);

        if (move_hacia_pp != null) {
            return move_hacia_pp;
        }

        MOVE[] posibles = game.getPossibleMoves(pacman_nodo, pacman_last_move);
        return posibles.length > 0 ? posibles[0] : MOVE.NEUTRAL;
    }

    @Override
    public String getActionId() {
        return "Acercarse A Power Pill";
    };
}