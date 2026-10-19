package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class FantasmasAgrupadosTransition implements Transition 
{
    private int radio_grupo;
    private int cantidad_minima;

    public FantasmasAgrupadosTransition(int radio_grupo, int cantidad_minima) 
    {
        this.radio_grupo = radio_grupo;
        this.cantidad_minima = cantidad_minima;
    }

    @Override
    public boolean evaluate(Input in) 
    {
        MsPacManInput input = (MsPacManInput) in;
        int count = 0;

        for (GHOST ghost : GHOST.values())
        {
            if (input.is_eatable(ghost)) 
            {
                int ghost_node = input.ghost_nodo_actual.get(ghost);
                int dist = input.getGame().getShortestPathDistance(input.pacman_nodo, ghost_node, input.pacman_last_move);
                
                if (dist != -1 && dist <= radio_grupo) 
                {
                    count++;
                }
            }
        }

        return count >= cantidad_minima;
    }

    @Override
    public String toString() 
    {
        return "Hay " + cantidad_minima + " o más fantasmas comestibles a menos de " + radio_grupo;
    }
}
