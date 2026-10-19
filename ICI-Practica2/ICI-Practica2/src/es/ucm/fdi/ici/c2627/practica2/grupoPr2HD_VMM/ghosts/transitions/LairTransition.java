package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.transitions;

import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.GhostsInput;

import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class LairTransition implements Transition {
	private boolean _opposite;
	private GHOST gh;
    public LairTransition(boolean opposite, GHOST gh) {
    	this.gh= gh;
    	_opposite = opposite;
    }

    @Override
    public boolean evaluate(Input in) {
        GhostsInput input = (GhostsInput) in;
        
        boolean bool = input.getGame().getGhostLairTime(gh) > 0;
        
        return _opposite? !bool : bool;
    }

    @Override
    public String toString() {
        return "Esta en celda";
    }
}
