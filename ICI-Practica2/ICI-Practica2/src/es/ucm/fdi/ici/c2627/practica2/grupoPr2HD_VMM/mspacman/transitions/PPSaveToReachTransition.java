package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class PPSaveToReachTransition implements Transition 
{
    private int margen;

    public PPSaveToReachTransition(int margen) 
    {
        this.margen = margen;
    }

    @Override
    public boolean evaluate(Input in) 
    {
        MsPacManInput input = (MsPacManInput) in;
        Game game = input.getGame();
        
        int pp_objetivo = input.closest_pp_nodo;

        if (pp_objetivo == -1) return false;

        int pacman_dist_a_pp = game.getShortestPathDistance(input.pacman_nodo, pp_objetivo, input.pacman_last_move);
        if (pacman_dist_a_pp == -1) return false;

        for (GHOST ghost : GHOST.values()) 
        {
            if (!input.is_eatable(ghost) && game.getGhostLairTime(ghost) == 0) 
            {
                int ghost_node = input.ghost_nodo_actual.get(ghost);
                int ghost_dist_a_pp = game.getShortestPathDistance(ghost_node, pp_objetivo, input.ghost_last_move.get(ghost));
                if (ghost_dist_a_pp != -1 && ghost_dist_a_pp <= pacman_dist_a_pp + margen) 
                {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "Power Pill alcanzable y segura";
    }
}