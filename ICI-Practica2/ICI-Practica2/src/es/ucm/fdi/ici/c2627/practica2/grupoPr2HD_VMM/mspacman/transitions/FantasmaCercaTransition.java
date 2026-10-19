package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.PacManHelper;
import es.ucm.fdi.ici.fsm.Transition;

public class FantasmaCercaTransition implements Transition
{
    private int limite_distancia;

    public FantasmaCercaTransition(int limite_distancia) {
        this.limite_distancia = limite_distancia;
    }

    @Override
    public boolean evaluate(Input in) {
        MsPacManInput input = (MsPacManInput) in;
        int distancia = PacManHelper.get_min_distance_from_dangerous_ghosts(in.getGame(), input);
        
        return distancia <= limite_distancia;
    }

    @Override
    public String toString() {
        return "Fantasma peligroso a menos de " + limite_distancia;
    }
}
