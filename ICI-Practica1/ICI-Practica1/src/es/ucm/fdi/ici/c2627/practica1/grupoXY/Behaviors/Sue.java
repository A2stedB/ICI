package es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors;

import es.ucm.fdi.ici.c2627.practica1.grupoXY.GhostsHelper;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Sue extends GhostBehavior {
    @Override
    public MOVE Chase(Game game, int gNode, int pNode, MOVE lMove) {
    	//Si está lejos ataca, si está a menos de 8 nodos se desvía a tapar salida
    	int targetNode = pNode;
        
    	if (game.getShortestPathDistance(gNode, pNode, lMove) < 8) {
            int[] junctions = game.getJunctionIndices();
            targetNode = game.getClosestNodeIndexFromNodeIndex(pNode, junctions, DM.PATH);
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