package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.transitions;

import static pacman.game.Constants.distNames;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.MOVE;

public class NodoAlcanzableSeguro implements Transition
{
    private int a;
    private MOVE a_last_move;
    private int b;
    private MOVE b_last_move;
    private int objective;

    public NodoAlcanzableSeguro(int a, MOVE a_last_move, int b, MOVE b_last_move,int objective) 
    {
        this.a = a;
        this.a_last_move = a_last_move;
        this.b = b;
        this.b_last_move = b_last_move;
        this.objective = objective;
        // this.cerca = cerca;
    }

    @Override
    public boolean evaluate(Input in) {
        MsPacManInput input = (MsPacManInput) in;
        
        
        int distance_a_to_o = input.getGame().getShortestPathDistance(a, objective, a_last_move);
        int distance_b_to_o = input.getGame().getShortestPathDistance(b, objective, b_last_move);

        if(distance_a_to_o < distance_b_to_o && distance_a_to_o - distance_b_to_o  < 5){
            return true;
        }

        return false;
    }

    @Override
    public String toString() 
    {
        return "Comprobando si hay fantasma cerca";
    }
}
