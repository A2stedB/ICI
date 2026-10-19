package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game; 

public class liarAction implements Action {

    private final GHOST ghost;      // qué fantasma, sí; el Game, nunca
    
    public liarAction(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public MOVE execute(Game game) {
    	return MOVE.NEUTRAL;
    }

    @Override
    public String getActionId() {
        return ghost + "lair";
    }
}