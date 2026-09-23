package es.ucm.fdi.ici.c2627.practica0.grupoIndividual;

import pacman.game.Game;

public class Helper 
{
    public static int PACMAN_get_closest_power_pill(Game game)
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

        return -1;
    };
}
