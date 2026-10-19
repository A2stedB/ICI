package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class PPCercaTransition implements Transition
{
    private int distance;

    public PPCercaTransition(int distance) 
    {
        this.distance = distance;
        // this.cerca = cerca;
    }

    @Override
    public boolean evaluate(Input in) {
        MsPacManInput input = (MsPacManInput) in;
        
        for(int nodo_pp : input.pp_disponible)
        {
            if(input.getGame().getShortestPathDistance(input.pacman_nodo, nodo_pp, input.pacman_last_move) < distance)
                return true;
        }

        return false;
    }

    @Override
    public String toString() 
    {
        return "Comprobando si hay fantasma cerca";
    }
}
