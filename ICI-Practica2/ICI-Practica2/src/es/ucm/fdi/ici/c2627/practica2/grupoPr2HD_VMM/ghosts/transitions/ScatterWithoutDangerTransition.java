package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.transitions;

import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.GhostsInput;

import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class ScatterWithoutDangerTransition implements Transition {
	private boolean _opposite;
	GHOST ghost;
    public ScatterWithoutDangerTransition (boolean opposite, GHOST g) {
    	_opposite = opposite;
    	ghost = g;
    }

    @Override
    public boolean evaluate(Input in) {
        GhostsInput input = (GhostsInput) in;
        Game game = in.getGame();
        boolean scatter = game.isGhostEdible(ghost)
                && !input.pacman_dangerously_close_to_PP(game, game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())
                && input.getScatterTimeLeft() > 0;
        
        return _opposite? !scatter : scatter;
    }

    @Override
    public String toString() {
        return "El fantasma no es comestible, Pacman no está lo suficientemente cerca de una Power Pill como para comerla y pillarle y ScatterTime > 0.";
    }
}
