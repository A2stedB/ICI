package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.transitions;

import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.GhostsInput;

import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class SOSTransition implements Transition {
	private boolean _opposite;
	
    public SOSTransition(boolean opposite) {
    	_opposite = opposite;
    }

    @Override
    public boolean evaluate(Input in) {
        GhostsInput input = (GhostsInput) in;
        return _opposite? !someEdible(input.getGame()) : someEdible(input.getGame());
    }

    @Override
    public String toString() {
        return "Hay al menos un fantasma NO comestible en el mapa.";
    }
    
    private boolean someEdible(Game game) {
    	GHOST[] ghosts = GHOST.values();
    	int i = 0;
    	boolean enc = false;
    	
    	while (i < ghosts.length && !enc) {
    		enc = game.getGhostEdibleTime(ghosts[i]) <= 0;
    		i++;
    	}
    	
    	return enc;
    }
}
