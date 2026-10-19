package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;

public class PPLeftTransition implements Transition
{
    public PPLeftTransition() {}

    @Override
    public boolean evaluate(Input in) 
    {
        return in.getGame().getNumberOfActivePowerPills() > 0;
    }

    @Override
    public String toString() {
        return "Queda al menos 1 PP";
    }
}
