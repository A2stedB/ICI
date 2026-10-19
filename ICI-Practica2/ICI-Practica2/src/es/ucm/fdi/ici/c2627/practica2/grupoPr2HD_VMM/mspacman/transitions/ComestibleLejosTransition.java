package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class ComestibleLejosTransition implements Transition 
{
    
    public boolean evaluate(Input in) 
    {
        MsPacManInput input = (MsPacManInput) in;
    
        return false;
    }
}
