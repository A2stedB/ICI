package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class PPWasEaten implements Transition
{
    private int n = 0;

    public PPWasEaten(int num)
    {
        this.n = num;
    }
    public boolean evaluate(Input in) 
    {
        return in.getGame().wasPowerPillEaten();
    }

    public String toString() 
    {
        return "Pac-Man comió Power Pill v" + n;
    }
}
