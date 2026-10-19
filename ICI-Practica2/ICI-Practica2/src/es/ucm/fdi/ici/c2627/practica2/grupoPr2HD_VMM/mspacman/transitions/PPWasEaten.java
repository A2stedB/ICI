package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class PPWasEaten implements Transition
{
    static int OFFSET = 1;

    public PPWasEaten()
    {
        PPWasEaten.OFFSET++;
    }
    public boolean evaluate(Input in) 
    {
        return in.getGame().wasPowerPillEaten();
    }

    public String toString() 
    {
        return "Pac-Man comió Power Pill " + OFFSET;
    }
}
