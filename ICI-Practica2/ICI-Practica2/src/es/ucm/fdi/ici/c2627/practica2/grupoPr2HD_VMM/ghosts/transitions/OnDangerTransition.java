package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.transitions;

import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.GhostsBrain;
import es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.GhostsInput;

import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class OnDangerTransition implements Transition {
	private boolean _opposite;
	GHOST ghost;
	GhostsBrain _brain;
	
    public OnDangerTransition(boolean opposite, GHOST g, GhostsBrain brain) {
    	_brain = brain;
    	_opposite = opposite;
    	ghost = g;
    }

    @Override
    public boolean evaluate(Input in) {
        GhostsInput input = (GhostsInput) in;
        Game game = in.getGame();
        boolean danger = game.isGhostEdible(ghost)
                || _brain.pacman_dangerously_close_to_PP(game, game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
        
        return _opposite? !danger : danger;
    }

    @Override
    public String toString() {
        return "El fantasma es comestible o Pacman está lo suficientemente cerca de una Power Pill como para comerla y pillarle y al fantasma no le da tiempo a alcanzar la píldora antes de Pacman en sentido contrario.";
    }
}
