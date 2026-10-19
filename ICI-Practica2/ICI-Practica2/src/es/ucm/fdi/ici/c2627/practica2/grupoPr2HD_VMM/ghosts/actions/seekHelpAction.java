package es.ucm.fdi.ici.c2627.practica2.grupoPr2HD_VMM.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game; 

public class seekHelpAction implements Action {

    private final GHOST ghost;      // qué fantasma, sí; el Game, nunca
    
    public seekHelpAction(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public MOVE execute(Game game) {
    	return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), getTarget(game), game.getGhostLastMoveMade(ghost), DM.PATH);
    }

    @Override
    public String getActionId() {
        return ghost + " run away";
    }
    
    private int getTarget(Game game) {
    	GHOST[] ghosts = GHOST.values();
    	GHOST g = ghosts[0];
    	int min = Integer.MAX_VALUE;
    	for (int i = 0; i < ghosts.length; i++) {
    		if (game.getGhostEdibleTime(ghosts[i]) <= 0 && game.getGhostLairTime(ghosts[i]) <= 0 && game.getGhostLairTime(ghost) <= 0) {
    			int a = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(ghosts[i]), game.getGhostLastMoveMade(ghost));
    			if (a < min) {
    				min = a;
    				g = ghosts[i];
    			}
    		}
    	}
    	
    	return game.getGhostCurrentNodeIndex(g);
    }
}