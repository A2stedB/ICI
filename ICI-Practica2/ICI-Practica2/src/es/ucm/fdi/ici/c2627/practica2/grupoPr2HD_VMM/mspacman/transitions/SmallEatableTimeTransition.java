package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.PacManHelper;
import es.ucm.fdi.ici.fsm.Transition;

public class SmallEatableTimeTransition implements Transition
{
    private int limite_tiempo;

    public SmallEatableTimeTransition(int limite_tiempo) 
    {
        this.limite_tiempo = limite_tiempo;
    }

    @Override
    public boolean evaluate(Input in) 
    {
        MsPacManInput input = (MsPacManInput) in;
        int max_tiempo_restante = PacManHelper.get_max_edible_time_remaining(input);
        
        return max_tiempo_restante == -1 || max_tiempo_restante < limite_tiempo;
    }

    @Override
    public String toString() 
    {
        return "Tiempo comestible menor a " + limite_tiempo;
    }
}
