package es.ucm.fdi.ici.c2627.practica1.grupoXY.Behaviors;

import es.ucm.fdi.ici.c2627.practica1.grupoXY.Helper;
import es.ucm.fdi.ici.c2627.practica1.grupoXY.GhostsHelper;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Sue extends GhostBehavior {
    @Override
    public MOVE Chase(Game game, int gNode, int pNode, MOVE lMove) {
    	int targetNode = pNode;
        MOVE pacDir = game.getPacmanLastMoveMade();
        
        //Si pacman se acerca a una pildora, Sue va a hacer de perrito guardián.
        if (game.getShortestPathDistance(gNode, pNode, lMove) < 28) {
            int closest_PP = Helper.PACMAN_get_closest_power_pill(game);
            
            if (closest_PP != -1 && pacDir != null) {
                int[] pacAhead = game.getShortestPath(pNode, game.getPacManInitialNodeIndex(), pacDir);
                
                boolean pacmanVaAPorLaPildora = false;
                if (pacAhead != null) {
                    int limiteCercania = Math.min(pacAhead.length, 30);
                    for (int i = 0; i < limiteCercania; i++) {
                        if (pacAhead[i] == closest_PP) {
                            pacmanVaAPorLaPildora = true;
                            break;
                        }
                    }
                }
                
                if (pacmanVaAPorLaPildora) {
                    int[] junctions = game.getJunctionIndices();
                    targetNode = game.getClosestNodeIndexFromNodeIndex(closest_PP, junctions, DM.PATH);
                } 
                else {
                    int[] junctions = game.getJunctionIndices();
                    targetNode = game.getClosestNodeIndexFromNodeIndex(pNode, junctions, DM.PATH);
                }
            }
        }
        
        MOVE bestMove = game.getApproximateNextMoveTowardsTarget(gNode, targetNode, lMove, DM.PATH);
        int nextNode = game.getNeighbour(gNode, bestMove);
        if (nextNode != -1 && game.isJunction(nextNode)) {
            int[] jctInfo = GhostsHelper.getNextJuctionOnWay(game, gNode, game.getShortestPath(gNode, targetNode, bestMove));
            if (jctInfo[0] == gNode) {
                for (MOVE m : game.getPossibleMoves(gNode, lMove)) {
                    if (m != bestMove) { bestMove = m; break; }
                }
            }
        }
        return bestMove;
    }
}