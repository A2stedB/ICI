package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class PPCloseTransition implements Transition
{
    private int radio_distancia;

    public PPCloseTransition(int radio_distancia) 
    {
        this.radio_distancia = radio_distancia;
    }

    @Override
    public boolean evaluate(Input in) {
        MsPacManInput input = (MsPacManInput) in;
        
        int closest_pp = input.closest_pp_nodo; 
        
        if (closest_pp != -1) {
            int distance = input.getGame().getShortestPathDistance(input.pacman_nodo, closest_pp, input.pacman_last_move);
            return distance <= radio_distancia;
        }
        
        return false;
    }

    @Override
    public String toString() {
        return "PP más cercana a " + radio_distancia + " o menos";
    }
}
