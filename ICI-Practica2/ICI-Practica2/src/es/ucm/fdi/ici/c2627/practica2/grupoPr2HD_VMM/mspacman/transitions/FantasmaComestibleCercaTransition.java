package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.PacManHelper;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class FantasmaComestibleCercaTransition implements Transition 
{
    private int limite_distancia;

    public FantasmaComestibleCercaTransition(int limite_distancia) {
        this.limite_distancia = limite_distancia;
    }

    @Override
    public boolean evaluate(Input in) 
    {
        MsPacManInput input = (MsPacManInput) in;
        int distancia = PacManHelper.get_min_distance_to_eatable_ghosts(in.getGame(), input);
        
        return distancia <= limite_distancia;
    }

    @Override
    public String toString() 
    {
        return "Fantasma comestible a menos de " + limite_distancia;
    }
}
