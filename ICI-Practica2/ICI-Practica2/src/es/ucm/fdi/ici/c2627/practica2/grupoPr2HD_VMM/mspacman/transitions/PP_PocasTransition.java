package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;

public class PP_PocasTransition implements Transition 
{
    private int limite_pildoras;

    public PP_PocasTransition(int limite_pildoras) 
    {
        this.limite_pildoras = limite_pildoras;
    }

    @Override
    public boolean evaluate(Input in) {
        return in.getGame().getNumberOfActivePills() <= limite_pildoras;
    }

    @Override
    public String toString() {
        return "Quedan " + limite_pildoras + " píldoras o menos";
    }
}
