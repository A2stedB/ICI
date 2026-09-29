package es.ucm.fdi.ici.c2627.practica0.grupoIndividual;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

import java.util.ArrayList;

import org.apache.commons.lang.ObjectUtils.Null;

import net.sourceforge.jFuzzyLogic.fcl.FclParser.linguistic_term_return;

public class MsPacMan extends pacman.controllers.PacmanController
{
    private Integer limit = 30;

    public MOVE getMove(Game game, long timeDue)
    {
        MOVE next_move = MOVE.NEUTRAL;

        // path_distances.clear();
        // int pacman_node_index = game.getPacmanCurrentNodeIndex();
        // for(GHOST ghost_type : GHOST.values())
        // {
        //     int ghost_node_index = game.getGhostCurrentNodeIndex(ghost_type);
        //     int path_distance =  game.getShortestPathDistance(ghost_node_index, pacman_node_index);
            
        //     GhostData data = new GhostData(ghost_node_index,path_distance);
        //     path_distances.add(data);
        // }
        
        // MOVE next_move = game.getApproximateNextMoveAwayFromTarget(pacman_node_index, path_distances.peek().node_index, lastMove, DM.PATH;
        return next_move;
    };

}
