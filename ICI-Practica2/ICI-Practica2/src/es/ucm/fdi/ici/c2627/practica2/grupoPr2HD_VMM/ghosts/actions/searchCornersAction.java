package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game; 

public class searchCornersAction implements Action {

    private final GHOST ghost;      // qué fantasma, sí; el Game, nunca
    
    public searchCornersAction(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public MOVE execute(Game game) {
    	return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getPowerPillIndices()[ghost.ordinal()], game.getGhostLastMoveMade(ghost), DM.PATH);
    }

    @Override
    public String getActionId() {
        return ghost + " searchCorners";
    }
}