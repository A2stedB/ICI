package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class NotTransition implements Transition 
{
    private Transition a;

    public NotTransition(Transition a) 
    {
		this.a = a;
    }

    @Override
    public boolean evaluate(Input in)
	{
		MsPacManInput input = (MsPacManInput)in;
		return !(a.evaluate(input));
    }

    @Override
    public String toString() 
    {
        return "No " + a.toString();
    }
}
