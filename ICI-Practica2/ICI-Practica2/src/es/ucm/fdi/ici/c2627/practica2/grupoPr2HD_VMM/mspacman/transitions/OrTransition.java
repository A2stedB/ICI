package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class OrTransition implements Transition
{
    private Transition a;
    private Transition b;

    public OrTransition(Transition a, Transition b) 
    {
		this.a = a;
		this.b = b;
    }

    @Override
    public boolean evaluate(Input in)
	{
		MsPacManInput input = (MsPacManInput)in;
		return a.evaluate(input) || b.evaluate(input);
    }

    @Override
    public String toString() 
    {
        return a.toString() + " o " + b.toString();
    }
}
