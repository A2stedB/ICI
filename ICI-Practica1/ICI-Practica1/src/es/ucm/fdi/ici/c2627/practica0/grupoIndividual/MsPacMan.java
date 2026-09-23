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

        GhostData nearest_ghost = get_nearest_ghost(game, DM.PATH);

        if(nearest_ghost != null)
        {
            if(!nearest_ghost.is_eatable){
                next_move = move_away_from_chasing_ghost(game, nearest_ghost);
                System.out.println("HUYENDO!");
            }
            else if(nearest_ghost.is_eatable){
                next_move = move_toward_eatable_ghost(game, nearest_ghost);
                System.out.println("Intentando comer");
            }
        }
        else
        {
            int closest_pill = get_nearest_pill_index(game);
            int pacman_node_index = game.getPacmanCurrentNodeIndex();
            next_move = game.getApproximateNextMoveTowardsTarget(pacman_node_index, closest_pill,game.getPacmanLastMoveMade(), DM.PATH);
        }

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

    private GhostData get_nearest_ghost(Game game,DM dm)
    {   
        // ArrayList<GhostData> every_ghost = new ArrayList<GhostData>();
        int pacman_node_index = game.getPacmanCurrentNodeIndex();
        GhostData nearest = null;

        for(GHOST ghost_type : GHOST.values())
        {
            int ghost_node_index = game.getGhostCurrentNodeIndex(ghost_type);
            int path_distance =  game.getShortestPathDistance(ghost_node_index, pacman_node_index);
            
            // GhostData data = new GhostData(ghost_node_index,path_distance,ghost_type,game.getGhostLastMoveMade(ghost_type));
            if(path_distance < limit && path_distance > 0 && path_distance != -1 && (nearest == null || path_distance < nearest.path_distance))
            {
                nearest = new GhostData(ghost_node_index,path_distance,ghost_type,game.getGhostLastMoveMade(ghost_type));
                nearest.is_eatable = game.isGhostEdible(ghost_type);
            }
        }

        return nearest;
    }

    private MOVE move_away_from_chasing_ghost(Game game,GhostData ghost)
    {
        MOVE next_move = MOVE.NEUTRAL;
        int pacman_node_index = game.getPacmanCurrentNodeIndex();

        int[] shortest_path_nodes = game.getShortestPath(ghost.node_index, pacman_node_index, ghost.last_move);

        int last_node_of_shortest_path = shortest_path_nodes[shortest_path_nodes.length-1];

        next_move = game.getApproximateNextMoveAwayFromTarget(pacman_node_index, last_node_of_shortest_path, next_move, DM.PATH);

        return next_move;
    }

    private MOVE move_toward_eatable_ghost(Game game, GhostData ghost)
    {
        MOVE next_move = MOVE.NEUTRAL;

        int pacman_node_index = game.getPacmanCurrentNodeIndex();

        next_move = game.getApproximateNextMoveTowardsTarget(pacman_node_index, ghost.node_index, game.getPacmanLastMoveMade(), DM.PATH);

        return next_move;
    }

    private int get_nearest_pill_index(Game game)
    {
        int pacman_node_index = game.getPacmanCurrentNodeIndex();

        int active_power_pill_count = game.getNumberOfActivePowerPills();
        if(active_power_pill_count != 0)
        {
            int[] active_power_pill_indexes = game.getActivePowerPillsIndices();
            int closest_index = active_power_pill_indexes[0];
            int closest_path_distance = game.getShortestPathDistance(pacman_node_index, closest_index); 

            for(int i = 1; i < active_power_pill_indexes.length - 1; ++i)
            {
                int path_distance = game.getShortestPathDistance(pacman_node_index, active_power_pill_indexes[i]);
                if(path_distance < closest_path_distance){
                    closest_path_distance = path_distance;
                }
            }

            return closest_index;
        }

        else if(game.getNumberOfActivePills() != 0)
        {
            int[] active_pill_indexes = game.getPillIndices();
            int closest_index = active_pill_indexes[0];
            int closest_path_distance = game.getShortestPathDistance(pacman_node_index, closest_index); 

            for(int i = 0; i < active_pill_indexes.length;++i){
                int path_distance = game.getShortestPathDistance(pacman_node_index, active_pill_indexes[i]);
                if(path_distance < closest_path_distance)
                {
                    closest_path_distance = path_distance;
                }
            }

            return closest_index;
        }

        return -1;
    }
}
