package es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors;

import es.ucm.fdi.ici.c2627.practica1.grupoXY.GhostsHelper;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;

public class Inky extends GhostBehavior {
    @Override
    public MOVE Chase(Game game, int gNode, int pNode, MOVE lMove) {
    	//Encierra a pacman combinandose con Blinky
    	int targetNode = pNode;
    	MOVE pacDir = game.getPacmanLastMoveMade();
        
    	int blinkyIdx = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
        if (blinkyIdx != -1) {
            int distBlinkyPac = game.getShortestPathDistance(blinkyIdx, pNode);
            int[] pacAhead = game.getShortestPath(pNode, game.getPacManInitialNodeIndex(), pacDir);
            if (pacAhead != null && pacAhead.length > 0) {
                targetNode = pacAhead[Math.min(pacAhead.length - 1, Math.max(1, distBlinkyPac / 2))];
            }
        } 
        
        MOVE bestMove = game.getApproximateNextMoveTowardsTarget(gNode, targetNode, lMove, DM.PATH);
        // Si el movimiento óptimo nos metiera en un bucle circular sobre nosotros mismos, 
        // elegimos la segunda mejor opción para romperlo.
        int nextNode = game.getNeighbour(gNode, bestMove);
        if (nextNode != -1 && game.isJunction(nextNode)) {
            int[] jctInfo = GhostsHelper.getNextJuctionOnWay(game, gNode, game.getShortestPath(gNode, targetNode, bestMove));
            if (jctInfo[0] == gNode) {
                MOVE[] options = game.getPossibleMoves(gNode, lMove);
                for (MOVE m : options) {
                    if (m != bestMove) { bestMove = m; break; }
                }
            }
        }
        
        return bestMove;
    }
}