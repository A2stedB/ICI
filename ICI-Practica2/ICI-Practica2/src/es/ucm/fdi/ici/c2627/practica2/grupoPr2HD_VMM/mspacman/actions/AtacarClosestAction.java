package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class AtacarClosestAction implements Action {

    public AtacarClosestAction() 
    {
    
    }

    public MOVE execute(Game game) {
        int pacman_nodo = game.getPacmanCurrentNodeIndex();
        
        if (!game.isJunction(pacman_nodo)) 
        {
            return MOVE.NEUTRAL;
        }
        MOVE pacman_last_move = game.getPacmanLastMoveMade();
        MsPacManInput input = new MsPacManInput(game);

        int target_ghost_node = -1;
        int min_distance = Integer.MAX_VALUE;

        for (GHOST ghost : GHOST.values()) 
        {
            if (input.is_eatable(ghost)) 
            {
                int ghost_node = game.getGhostCurrentNodeIndex(ghost);
                int dist = game.getShortestPathDistance(pacman_nodo, ghost_node, pacman_last_move);
                if (dist != -1 && dist < min_distance) 
                {
                    min_distance = dist;
                    target_ghost_node = ghost_node;
                }
            }
        }

        if (target_ghost_node != -1) 
        {
            MOVE ataque = game.getNextMoveTowardsTarget(pacman_nodo, target_ghost_node, pacman_last_move, DM.PATH);
            if (ataque != null) return ataque;
        }

        MOVE[] posibles = game.getPossibleMoves(pacman_nodo, pacman_last_move);
        return posibles.length > 0 ? posibles[0] : MOVE.NEUTRAL;
    }

    @Override
    public String getActionId() 
    {
        return "Atacar Fantasma Más Cercano";
    }
}
