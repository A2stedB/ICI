package es.ucm.fdi.ici.c2627.practica0.grupoIndividual;

import java.util.EnumMap;
import java.util.Random;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends pacman.controllers.GhostController
{
    private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
    private int limit = 10;
    private Random rnd = new Random();

    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {

        int pacman_node_index = game.getPacmanCurrentNodeIndex();

        for (GHOST ghostType : GHOST.values()) {
            MOVE next_move = MOVE.NEUTRAL;
            int ghost_node_index = game.getGhostCurrentNodeIndex(ghostType);

            if (game.doesGhostRequireAction(ghostType) || pacman_close_to_power_pill(game,limit)) 
            {
                next_move = game.getNextMoveAwayFromTarget(pacman_node_index, game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
                // MOVE last_move = moves.get(ghostType);
                // int ghost_node_index = game.getGhostCurrentNodeIndex(ghostType);
                // MOVE next_move = game.getApproximateNextMoveTowardsTarget(ghost_node_index, pacman_node_index, last_move, DM.PATH);
                moves.put(ghostType, next_move);
            }
            else
            {
                float odd = rnd.nextInt(0,101);
                if (odd < 90)
                {
                    MOVE last_move = moves.get(ghostType);
                    next_move = game.getApproximateNextMoveTowardsTarget(ghost_node_index, pacman_node_index, last_move, DM.PATH);
                }
                else
                {
                    MOVE[] allMoves = MOVE.values();
                    moves.put(ghostType, allMoves[rnd.nextInt(allMoves.length)]);
                }
            }
        }
        return moves;
    }

    private boolean pacman_close_to_power_pill(Game game,int limit)
    {
        int pacman_node_index = game.getPacmanCurrentNodeIndex();
        int closest_power_pill_index = Helper.PACMAN_get_closest_power_pill(game);

        if(game.getShortestPathDistance(pacman_node_index, closest_power_pill_index) < limit)
        {
            return true;
        }
        return false;
    }
}
