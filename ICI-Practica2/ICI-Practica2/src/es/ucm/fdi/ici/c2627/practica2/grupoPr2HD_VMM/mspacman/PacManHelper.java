package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman;

import java.util.EnumMap;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import java.util.Map;
public class PacManHelper 
{

    // Funcion que dado un nodo y su movimiento, devolver las intersecciones que llegara si hace el move
    static public Map<MOVE, Integer> getNextIntersections(Game game, int current_node, MOVE last_move) 
    {
        Map<MOVE, Integer> intersections = new EnumMap<>(MOVE.class);

        MOVE[] possible_moves = game.getPossibleMoves(current_node, last_move);
        
        for (MOVE move : possible_moves) {
            int sig = game.getNeighbour(current_node, move);
            MOVE currentMove = move;

            while (sig != -1 && !game.isJunction(sig)) 
            {
                MOVE[] nextMoves = game.getPossibleMoves(sig, currentMove);
                
                if (nextMoves.length == 0) 
                {
                    break;
                }
                currentMove = nextMoves[0];
                sig = game.getNeighbour(sig, currentMove);
            }
            if (sig != -1 && game.isJunction(sig)) 
            {
                intersections.put(move, sig);
            }
        }
        
        return intersections;
    }

    // Devuelve la distancia mas corta del fantasma al pacman
    public static int get_min_distance_from_dangerous_ghosts(Game game, MsPacManInput input) 
    {
        int min_distance = Integer.MAX_VALUE;

        for (GHOST ghost : GHOST.values()) 
        {
            if (input.ghost_nodo_actual.get(ghost) != game.getGhostInitialNodeIndex() && !input.is_eatable(ghost)) {
                
                int ghost_node = input.ghost_nodo_actual.get(ghost);
                MOVE ghost_move = input.ghost_last_move.get(ghost);

                int distance = game.getShortestPathDistance(ghost_node, input.pacman_nodo, ghost_move);
                
                if (distance != -1 && distance < min_distance) {
                    min_distance = distance;
                }
            }
        }

        return min_distance;
    }

    // La distancia del pacman al comestible mas cercnaa
    public static int get_min_distance_to_eatable_ghosts(Game game, MsPacManInput input) 
    {
        int min_distance = Integer.MAX_VALUE;

        for (GHOST ghost : GHOST.values()) {
            if (input.is_eatable(ghost)) 
            {
                
                int ghost_node = input.ghost_nodo_actual.get(ghost);

                int distance = game.getShortestPathDistance(input.pacman_nodo, ghost_node, input.pacman_last_move);
                
                if (distance != -1 && distance < min_distance) {
                    min_distance = distance;
                }
            }
        }
        return min_distance;
    }


    public static int get_max_edible_time_remaining(MsPacManInput input) 
    {
        int max_time = -1;
        for (GHOST ghost : GHOST.values()) 
        {
            if (input.is_eatable(ghost)) 
            {
                int time = input.remaining_eatable_time.get(ghost);
                if (time > max_time) 
                {
                    max_time = time;
                }
            }
        }
        return max_time;
    }
}
