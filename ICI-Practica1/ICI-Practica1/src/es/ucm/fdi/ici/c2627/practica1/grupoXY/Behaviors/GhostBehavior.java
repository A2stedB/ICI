package es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors;

import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public abstract class GhostBehavior {
    public MOVE Avoid(Game game, int gNode, int pNode, MOVE lMove) {
    	MOVE next_move = game.getApproximateNextMoveAwayFromTarget(gNode, pNode, lMove, DM.PATH);
        return next_move;
    }
    
    public MOVE Scatter(Game game, int gNode, MOVE lMove, int corner) {
    	return game.getApproximateNextMoveTowardsTarget(gNode, game.getPowerPillIndices()[corner], lMove, DM.PATH);
    }
    
    public abstract MOVE Chase(Game game, int gNode, int pNode, MOVE lMove);
}