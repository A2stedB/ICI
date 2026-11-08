package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman;

import java.util.EnumMap;

import es.ucm.fdi.ici.Input;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacManInput extends Input
{
    public EnumMap<GHOST,Boolean> comestible;
    public EnumMap<GHOST,Integer> ghost_nodo_actual;
    public EnumMap<GHOST,MOVE> ghost_last_move;
    public EnumMap<GHOST,Integer> remaining_eatable_time;
    public int pacman_nodo;
    public MOVE pacman_last_move;
    public int[] pp_disponible;
    public int closest_pp_nodo;
    public int closest_eatable_distance;
    // public int remaining_edible_time;

    public MsPacManInput(Game game)
    {
        super(game);

    };

    public void parseInput()
    {
        comestible = new EnumMap<>(GHOST.class);
        ghost_nodo_actual = new EnumMap<>(GHOST.class);
        ghost_last_move = new EnumMap<>(GHOST.class);
        remaining_eatable_time = new EnumMap<>(GHOST.class);
        
        pacman_nodo = game.getPacmanCurrentNodeIndex();
        closest_eatable_distance = Integer.MAX_VALUE;
        pacman_last_move = game.getPacmanLastMoveMade();

        for(GHOST ghost : GHOST.values()){
            comestible.put(ghost, game.isGhostEdible(ghost));
            ghost_nodo_actual.put(ghost,game.getGhostCurrentNodeIndex(ghost));
            ghost_last_move.put(ghost,game.getGhostLastMoveMade(ghost));

            if(is_eatable(ghost)){
                remaining_eatable_time.put(ghost,game.getGhostEdibleTime(ghost));
                int pacman_to_eatable_distance = game.getShortestPathDistance(pacman_nodo, ghost_nodo_actual.get(ghost), pacman_last_move);
                if(pacman_to_eatable_distance < closest_eatable_distance)
                {
                    closest_eatable_distance = pacman_to_eatable_distance;
                }
            }
            else{
                remaining_eatable_time.put(ghost,-1);
            }
        }

        pp_disponible = game.getActivePowerPillsIndices();

        closest_pp_nodo = -1;
        int closest_pacman_pp_distance = Integer.MAX_VALUE;
        for(int node : pp_disponible)
        {
            int distance = game.getShortestPathDistance(pacman_nodo, node, pacman_last_move);
            if(distance < closest_pacman_pp_distance){
                closest_pp_nodo = node;
                closest_pacman_pp_distance = distance;
            }
        }
        if(pp_disponible.length == 0) closest_pp_nodo = -1;

        
    }

    public boolean is_eatable(GHOST ghost)
    {
        return comestible.get(ghost);
    }
}
