package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class ComestibleLejosTransition_v1 implements Transition 
{
    private int far_away_distance;

    public ComestibleLejosTransition_v1(int distance)
    {
        this.far_away_distance = distance;
    }
    
    public boolean evaluate(Input in) 
    {
        MsPacManInput input = (MsPacManInput) in;
        
        return input.closest_eatable_distance > this.far_away_distance;
    }
}
